import json
import datetime

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


def time_add(_time, _add):
    time = datetime.datetime(
        year=int(_time / 1000000),
        month=int(_time / 10000) % 100,
        day=int(_time / 100) % 100,
        hour=int(_time % 100)
    )
    if _add >= 0:
        time += datetime.timedelta(hours=int(_add / 60))
    else:
        time -= datetime.timedelta(hours=int(-_add / 60))
    return int("%02d%02d%02d%02d" % (time.year % 100, time.month, time.day, time.hour))


def overlaps(user, start_time, end_time):
    # lvl1 - start-time check
    if Event.objects.all().filter(owner=user, startTime__range=(start_time, end_time - 1)).exists():
        return True
    elif Event.objects.all().filter(owner=user, endTime__range=(start_time + 1, end_time)).exists():
        return True
    elif Event.objects.all().filter(owner=user, startTime__lte=start_time, endTime__gte=end_time).exists():
        return True


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
            and 'interventionReminder' in json_body and 'stressType' in json_body and 'stressCause' in json_body \
            and 'repeatMode' in json_body:
        if is_user_valid(json_body['username'], json_body['password']) and not Event.objects.all().filter(eventId=json_body['event_id']).exists() \
                and not overlaps(User.objects.get(username=json_body['username']), start_time=json_body['startTime'], end_time=json_body['endTime']):
            Event.objects.create_event(
                event_id=json_body['event_id'],
                owner=User.objects.get(username=json_body['username']),
                title=json_body['title'],
                stress_level=json_body['stressLevel'],
                start_time=json_body['startTime'],
                end_time=json_body['endTime'],
                intervention=json_body['intervention'],
                interv_reminder=json_body['interventionReminder'],
                stress_type=json_body['stressType'],
                stress_cause=json_body['stressCause'],
                repeat_mode=json_body['repeatMode']
            ).save()
            return Res(data={'result': RES_SUCCESS})
        else:
            return Res(data={'result': RES_FAILURE})
    else:
        return Res(data={'result': RES_BAD_REQUEST, 'reason': 'Some arguments are missing in the POST request!'})


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
            if 'startTime' in json_body and 'end_time' in json_body and not overlaps(User.objects.get(username=json_body['username']), start_time=json_body['startTime'], end_time=json_body['endTime']):
                event.startTime = json_body['startTime']
                event.endTime = json_body['endTime']
            if 'intervention' in json_body:
                event.intervention = json_body['intervention']
            if 'interventionReminder' in json_body:
                event.intervention = json_body['interventionReminder']
            if 'stressType' in json_body:
                event.stressType = json_body['stressType']
            if 'stressCause' in json_body:
                event.stressCause = json_body['stressCause']
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
def handle_events_fetch(request):
    json_body = json.loads(request.body.decode('utf-8'))
    if 'username' in json_body and 'password' in json_body and is_user_valid(json_body['username'], json_body['password']):
        user = User.objects.get(username=json_body['username'])
        _from = json_body['period_from']
        _till = json_body['period_till']
        result = {}
        done_ids = []
        array = []

        for event in Event.objects.all().filter(owner=user, startTime__range=(_from, _till - 1)):
            array.append(event.__json__())
            done_ids.append(event.eventId)
        for event in Event.objects.all().filter(owner=user, endTime__range=(_from + 1, _till)):
            if event.eventId not in done_ids:
                array.append(event.__json__())
        for event in Event.objects.all().filter(owner=user, startTime__lte=_from, endTime__gte=_till):
            if event.eventId not in done_ids:
                array.append(event.__json__())

        result['result'] = RES_SUCCESS
        result['array'] = array
        return Res(data=result)
    return Res(data={'result': RES_BAD_REQUEST})


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
