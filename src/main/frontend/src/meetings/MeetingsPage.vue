<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: [],
            };
        },
        methods: {
            async addNewMeeting(meeting) {
                await this.$http.post('meetings', meeting);
                await this.getAllMeetings();
            },

            async addMeetingParticipant(meeting) {
                var participantBody = {"login": this.username};
                await this.$http.post('meetings/' + meeting.id + '/participants', participantBody);
                await this.getAllMeetings();
            },

            async removeMeetingParticipant(meeting) {
                await this.$http.delete('meetings/' + meeting.id + '/participants/' + this.username);
                await this.getAllMeetings();

            },

            async getMeetingParticipants(meeting) {
                var participantList = [];
                var participantsResp = (await this.$http.get('meetings/' + meeting.id + '/participants')).body;
                for (var participantResp of participantsResp) {
                    participantList.push(participantResp.login);
                }
                return participantList;
            },

            async deleteMeeting(meeting) {
                await this.$http.delete('meetings/' + meeting.id);
                await this.getAllMeetings();
            },

            async getAllMeetings() {
                this.meetings = [];
                var meetingsResp = [];
                var meetingsResp = (await this.$http.get('meetings')).body;
                for (var meeting of meetingsResp) {
                    meeting.participants = await this.getMeetingParticipants(meeting);
                    this.meetings.push(meeting);
                }
            },

        },
        async created() {
            await this.getAllMeetings();
        }

    }
</script>
