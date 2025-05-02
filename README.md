# react-native-google-leaderboards-and-achievements

A React Native module for integrating Google Play Games Services on Android. Supports leaderboard score submissions, achievement unlocking and incrementing, and launching the native UI for leaderboards and achievements. Ideal for cross-platform mobile games requiring native-level game service features. using **Google Play Games v2 SDK**.

This library handles:
- Google Sign-In (V2 API)
- Leaderboard submission
- Leaderboard UI launch
- ACHIEVEMENTS UI launch
- Achievements Unlock
- Achievements Increment
- Auto-patching of `AndroidManifest.xml`, `build.gradle`, and Play Games configuration using your `app.json`

## BEFORE YOU USE THIS LIBRARY! MAKE SURE YOU HAVE ENABLED AND CONFIGURED GAMES SERVICES IN YOUR APP UNDER PLAY CONSOLE - ONLY THING YOU NEED IS TO ADD THE BLOCK IN YOUR APP.JSON AS STEP 1 BELOW

---

## 🚀 Features

- ✅ Sign-in to Google Play Games using `GamesSignInClient`
- ✅ Submit leaderboard scores
- ✅ Show native leaderboard UI
- ✅ Auto-patches Android `build.gradle`, `AndroidManifest.xml`, and Play Games metadata
- ✅ Fetches Google Play Games `projectId` from `app.json`

---

## 📦 Installation

```bash
npm install react-native-google-leaderboards-and-achievements
# or
yarn add react-native-google-leaderboards-and-achievements
```
1. Add the following to your root app.json 🚫 outside expo block:

```js

{
  "expo": {...},
  "react-native-google-leaderboards-and-achievements": {
    "projectId": "YOUR_GOOGLE_PLAY_GAMES_PROJECT_ID"
  }
}
```

⚠️ Important: Do NOT place this inside the expo block.
This library works with bare React Native (even if you use Expo dev tools).

2. Sync and rebuild

```bash
cd android && ./gradlew clean && cd ..
npx react-native run-android 
OR
npx expo run-android
OR
eas build ...

```

✅ Usage
1. Import the module:
```js
import {login} from 'react-native-google-leaderboards-and-achievements';
```

2. Sign In / check if authenticated:
```js
 const handleLogin = async () => {
   
    try {
      const result = await login();
      console.log('login result', JSON.stringify(result, null, 2))
      const parsed = JSON.parse(result);
    } catch (e) {
      console.log("Login error", e)
    } 
  };


  const handleCheckAuth = async () => {
    
    try {
      const result = await checkAuth();
      console.log('handleCheckAuth result', JSON.stringify(result, null, 2))
      const parsed = JSON.parse(result);
    } catch (e) {
      console.log("handleCheckAuth error", e)
    } 
  };
```

3. Submit a score:
```js
import {checkAuth, submitScore} from 'react-native-google-leaderboards-and-achievements';

checkAuth() //Call this before submitting score OPTIONAL
submitScore({
  leaderboardId: 'CgkI...yourLeaderboardId', //retrieve from login or checkAuth
  score: 1500 //properly format this before submitting
});

//Read https://developer.android.com/games/pgs/leaderboards#score_formatting about formatting. Before you submit formatted scores.

```

4. Show leaderboard UI:

```js
import {showLeaderboard, onShowLeaderboardsRequested} from 'react-native-google-leaderboards-and-achievements';

onShowLeaderboardsRequested() // show all leaderboards list
showLeaderboard('CgkI...yourLeaderboardId'); //particular leaderboard
```

# Achievements
 Show Achievements UI:

```js
import {showAchievements, unlockAchievement, incrementAchievement } from 'react-native-google-leaderboards-and-achievements';

showAchievements()
unlockAchievement('ACHIEVEMENT_ID');
incrementAchievement('ACHIEVEMENT_ID', 2);

```

📄 API Reference
login_v2(): Promise<string>
Authenticates the user with Google Play Games. Returns result or error.

submitScore({ leaderboardId, score })
Submits a score to a specific leaderboard.

Read https://developer.android.com/games/pgs/leaderboards#score_formatting about formatting. Before you submit formatted scores.

```js
  login(): Promise<string>;
  checkAuth(): Promise<string>;
  submitScore(leaderboardId: string, score: number): Promise<string>;
  onShowLeaderboardsRequested(): Promise<string>;
  showLeaderboard(leaderboardId: string): Promise<string>;

  //achievements

  showAchievements(): Promise<string>;
  unlockAchievement(my_achievement_id: string): Promise<string>;
  incrementAchievement(my_achievement_id: string, steps: number): Promise<string>;

  ```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
