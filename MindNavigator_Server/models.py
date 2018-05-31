from django.db import models


class UserManager(models.Manager):
    def create_user(self, username, password, name):
        return self.create(username=username, password=password, email=name)


class User(models.Model):
    username = models.CharField(max_length=32, primary_key=True)
    password = models.CharField(max_length=32)
    email = models.CharField(max_length=64)
    objects = UserManager()


class EventManager(models.Manager):
    def create_event(self, event_id, owner, title, stress_level, start_time, end_time):
        return self.create(eventId=event_id, owner=owner, title=title, stressLevel=stress_level, startTime=start_time, endTime=end_time)


class Event(models.Model):
    eventId = models.BigIntegerField(primary_key=True)
    owner = models.OneToOneField(to=User, on_delete=models.CASCADE)
    title = models.CharField(max_length=128, default='unnamed')
    stressLevel = models.PositiveSmallIntegerField()
    startTime = models.BigIntegerField()
    endTime = models.BigIntegerField()
    objects = EventManager()


class InterventionManager(models.Manager):
    PEER = 'peer'
    SYSTEM = 'system'

    def create_intervention(self, name, intervention_type):
        return self.create(name=name, interventionType=intervention_type)


class Intervention(models.Model):
    name = models.CharField(max_length=128, primary_key=True)
    interventionType = models.CharField(max_length=6)
    objects = InterventionManager()
