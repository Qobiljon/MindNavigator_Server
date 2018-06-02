import json

from rest_framework.decorators import api_view
from rest_framework.response import Response as Res
from MindNavigator_Server.models import User, Event, Intervention, InterventionManager, Evaluation, Feedback

# region Constants
RES_SUCCESS = 0
RES_FAILURE = 1
RES_BAD_REQUEST = -1


# endregion


def is_user_valid(username, password):
    if User.objects.filter(username=username).exists():
        user = User.objects.get(username=username)
        return user.password == password
    return False


@api_view(['POST'])
def handle_register(request):
    json_body = json.loads(request.body.decode('utf-8'))

    if 'username' in json_body and 'password' in json_body and 'name' in json_body:
        username = json_body['username']
        password = json_body['password']
        name = json_body['name']

        if User.objects.filter(username=username).exists():
            return Res(data={'result': RES_FAILURE})
        else:
            User.objects.create_user(username=username, password=password, name=name)
            return Res(data={'result': RES_SUCCESS})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'either of username, password, or name was not passed as a POST argument!'})


@api_view(['POST'])
def handle_login(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body:
        if is_user_valid(json_body['username'], json_body['password']):
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Username or Password was not passed as a POST argument!'})


@api_view(['POST'])
def handle_event_create(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body and 'event_id' in json_body and 'title' in json_body \
            and 'stressLevel' in json_body and 'startTime' in json_body and 'endTime' in json_body and 'intervention' in json_body \
            and 'stressType' in json_body and 'stressCause' in json_body and 'isShared' in json_body and 'repeatMode' in json_body:
        if is_user_valid(json_body['username'], json_body['password']) and not Event.objects.all().filter(eventId=json_body['event_id']).exists():
            Event.objects.create_event(
                event_id=json_body['event_id'],
                owner=User.objects.get(username=json_body['username']),
                title=json_body['title'],
                stress_level=json_body['stressLevel'],
                start_time=json_body['startTime'],
                end_time=json_body['endTime'],
                intervention=json_body['intervention'],
                stress_type=json_body['stressType'],
                stress_cause=json_body['stressCause'],
                is_shared=json_body['isShared'],
                repeat_mode=json_body['repeatMode']
            ).save()
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Username, password, or event_id was not passed as a POST argument!'})


@api_view(['POST'])
def handle_event_edit(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body and 'event_id' in json_body:
        if is_user_valid(json_body['username'], json_body['password']) and Event.objects.all().filter(owner__username=json_body['username'], eventId=json_body['event_id']).exists():
            event = Event.objects.get(eventId=json_body['event_id'])
            if 'stressLevel' in json_body:
                event.stressLevel = json_body['stressLevel']
            if 'title' in json_body:
                event.title = json_body['title']
            if 'startTime' in json_body:
                event.startTime = json_body['startTime']
            if 'endTime' in json_body:
                event.endTime = json_body['endTime']
            if 'intervention' in json_body:
                event.intervention = json_body['intervention']
            if 'stressType' in json_body:
                event.stressType = json_body['stressType']
            if 'stressCause' in json_body:
                event.stressCause = json_body['stressCause']
            if 'isShared' in json_body:
                event.isShared = json_body['isShared']
            if 'repeatMode' in json_body:
                event.repeatMode = json_body['repeatMode']
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Username, password, or event_id was not passed as a POST argument!'})


@api_view(['POST'])
def handle_event_delete(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body and 'event_id' in json_body:
        if is_user_valid(json_body['username'], json_body['password']) and Event.objects.all().filter(owner__username=json_body['username'], eventId=json_body['event_id']).exists():
            Event.objects.get(eventId=json_body['event_id']).delete()
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Username, password, or event_id was not passed as a POST argument!'})


@api_view(['POST'])
def handle_intervention_create(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body and 'interventionName' in json_body:
        if is_user_valid(json_body['username'], json_body['password']) and not Intervention.objects.all().filter(name=json_body['interventionName']).exists():
            Intervention.objects.create_intervention(name=json_body['interventionName'], intervention_type=InterventionManager.PEER).save()
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Username, password, or event_id was not passed as a POST argument!'})


@api_view(['POST'])
def handle_system_intervention_get(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body:
        if is_user_valid(json_body['username'], json_body['password']):
            array = []
            for intervention in Intervention.objects.all().filter(interventionType=InterventionManager.SYSTEM):
                array.append(intervention.name)
            return Res(data={'result': RES_SUCCESS, 'names': array})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Username, password, or event_id was not passed as a POST argument!'})


@api_view(['POST'])
def handle_peer_intervention_get(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body:
        if is_user_valid(json_body['username'], json_body['password']):
            array = []
            for intervention in Intervention.objects.all().filter(interventionType=InterventionManager.PEER):
                array.append(intervention.name)
            return Res(data={'result': RES_SUCCESS, 'names': array})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Username, password, or event_id was not passed as a POST argument!'})


@api_view(['POST'])
def handle_evaluation_submit(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body and 'eventId' in json_body and 'interventionName' in json_body and 'startTime' in json_body and 'endTime' in json_body \
            and 'eventDone' in json_body and 'interventionDone' in json_body and 'interventionDoneBefore' in json_body and 'recommend' in json_body:
        if is_user_valid(json_body['username'], json_body['password']):
            Evaluation.objects.create_evaluation(
                user=User.objects.get(username=json_body['username']),
                event_id=json_body['eventId'],
                intervention_name=json_body['interventionName'],
                start_time=json_body['startTime'],
                end_time=json_body['endTime'],
                event_done=json_body['eventDone'],
                intervention_done=json_body['interventionDone'],
                intervention_done_before=json_body['interventionDoneBefore'],
                recommend=json_body['recommend']
            ).save()
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Some arguments are not present to complete this POST request!'})


@api_view(['POST'])
def handle_feedback_submit(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body and 'startTime' in json_body and 'endTime' in json_body and 'done' in json_body:
        if is_user_valid(json_body['username'], json_body['password']):
            Feedback.objects.create_feedback(
                user=User.objects.get(username=json_body['username']),
                start_time=json_body['startTime'],
                end_time=json_body['endTime'],
                done=json_body['done']
            ).save()
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Some arguments are not present to complete this POST request!'})
