# react-native-google-leaderboards

A lightweight React Native module to integrate **Google Play Games Services** Leaderboards into your Android apps using **Google Play Games v2 SDK**.

This library handles:
- Google Sign-In (V2 API)
- Leaderboard submission
- Leaderboard UI launch
- Auto-patching of `AndroidManifest.xml`, `build.gradle`, and Play Games configuration using your `app.json`

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
npm install react-native-google-leaderboards
# or
yarn add react-native-google-leaderboards
```
1. Add the following to your root app.json 🚫 outside expo block:

```js
{
  "react-native-google-leaderboards": {
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
import {login} from 'react-native-google-leaderboards';
```

2. Sign In:
```js
login()
  .then(res => {
    console.log('Login successful:', res);
  })
  .catch(err => {
    console.error('Login failed:', err);
  });
```

3. Submit a score:
```js
import {checkAuth, submitScore} from 'react-native-google-leaderboards';

checkAuth() //Call this before submitting score
submitScore({
  leaderboardId: 'CgkI...yourLeaderboardId', //retrieve from login or checkAuth
  score: 1500
});

```

4. Show leaderboard UI:

```js
import {showLeaderboard} from 'react-native-google-leaderboards';

showLeaderboard('CgkI...yourLeaderboardId');
```

📄 API Reference
login_v2(): Promise<string>
Authenticates the user with Google Play Games. Returns result or error.

submitScore({ leaderboardId, score })
Submits a score to a specific leaderboard.

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
