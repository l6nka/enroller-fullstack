<template>
  <div id="app">
    <h1>
      <img src="./assets/logo.svg" alt="Enroller" class="logo">
      System do zapisów na zajęcia
    </h1>
    <div v-if="authenticatedUsername">
      <h2>Witaj {{ authenticatedUsername }}!
        <a @click="logout()" class="float-right  button-outline button">Wyloguj</a>
      </h2>
      <meetings-page :username="authenticatedUsername"></meetings-page>
    </div>
    <div v-else>
      <button @click="registering = false" :class="registering ? 'button-outline' : ''">Logowanie</button>
      <button @click="registering = true">Rejestruj się</button>
      <div v-if="error" class="error-msg">{{ error }}</div>
      <login-form @login="login($event)" v-if="registering == false"></login-form>
      <login-form @login="register($event)" button-label="Zarejestruj się" v-if="registering == true"></login-form>
    </div>
  </div>
</template>

<script>
    import "milligram";
    import LoginForm from "./LoginForm";
    import MeetingsPage from "./meetings/MeetingsPage";

    export default {
        components: {LoginForm, MeetingsPage},
        data() {
            return {
                authenticatedUsername: "",
                registering: false,
                error: ''
            };
        },
        methods: {
            login(user) {
                this.authenticatedUsername = user.login;
            },
            register(user) {
                this.error = '';
                this.$http.post('participants', user)
                    .then(response => {
                      // ok
                    })
                    .catch(response => {
                      if (response.status == 409) {
                        this.error = "Taki użytkownik już istnieje";
                      }
                    })
            },
            logout() {
                this.authenticatedUsername = '';
            }
        }
    };
</script>

<style>
  #app {
    max-width: 1000px;
    margin: 0 auto;
  }

  .logo {
    vertical-align: middle;
  }

  .error-msg {
    padding: 10 px;
    border: 2px outset rgba(164,71,71,0.07);
    -webkit-box-shadow: 5px 5px 15px 5px #FF3E7B;
    box-shadow: 5px 5px 15px 5px #FF3E7B;
  }
</style>

