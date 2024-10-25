# Profile Saving App - Viet Hoang Pham
## Overview
This is a profile saving mobile app allows users to see, create, update, and manage profiles. 

## Features
- **Profile Details**: Display profiles with attributes such as name, date of birth, category, phone number, and home address.
- **ViewModel**: Store persistent data, and also helps activities and fragments communicate with each other
- **LiveData**: It is an observable data store class. It only update data when they need to, which helps our app run faster.
- **Local Database**: Room is used to store profiles locally on the user's device
- **Remote Database**: Firestore is used to store profiles remotely, so the users can restore these profiles in another devices.
- **UI Widgets**: Uses widgets such as ChipGroup, ... to enhance user interaction.
- **Concurrency**: Coroutines are used to run background tasks, without blocking the main UI thread.

## Setup Instructions
- **Clone** this repository:
```
1. git clone https://github.com/SoftDevMobDev-2024-Classrooms/assignment03-Rinekochan.git
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or a physical device.
```
