# MedhaWeatherApp - Android Weather Forecast App

**MedhaWeatherApp** is an Android application that allows users to check real-time weather forecasts for any U.S. ZIP code. Built in **Java using Android Studio in 2023**, the app demonstrates working with APIs, JSON parsing, asynchronous tasks, and interactive UI elements.

---

## **Features**

### Weather Information
- **Current Temperature**: Displays temperature in Fahrenheit.
- **Feels Like Temperature**: Shows perceived temperature for better accuracy.
- **Weather Description**: Text description of current weather conditions (e.g., clear sky, rain, snow).
- **Wind Speed**: Displays wind speed in mph.
- **Weather Icons**: Dynamic images update based on weather conditions (sun, clouds, rain, snow, thunder, mist).

### User Interaction
- **ZIP Code Input**: Users can enter any U.S. ZIP code to get local weather.
- **Time Slider**: A `SeekBar` allows users to browse hourly forecasts.
- **Responsive UI**: Widgets update automatically when the user changes the ZIP code or moves the slider.

---

## **How It Works**

1. **User Input**
   - Users enter a ZIP code and press the **Check Weather** button.

2. **AsyncTask for API Requests**
   - An `AsyncTask` fetches data from the **OpenWeatherMap API** in the background.
   - JSON data is parsed for temperature, weather description, wind speed, and timestamps.

3. **Updating UI**
   - After fetching data, the app updates:
     - Temperature (`TextView`)
     - Feels like temperature (`TextView`)
     - Weather description (`TextView`)
     - Wind speed (`TextView`)
     - Weather icon (`ImageView`)
   - The `SeekBar` lets users scroll through hourly forecasts and see updates in real-time.

---

## **Technical Highlights**
- **Languages & Frameworks**: Java, Android Studio  
- **Android Components**: `TextView`, `ImageView`, `Button`, `SeekBar`, `EditText`  
- **Networking**: Asynchronous API calls with `AsyncTask`  
- **Data Handling**: JSON parsing for OpenWeatherMap API responses  
- **Time Conversion**: Converts API timestamps to local time for display  
- **Dynamic UI Updates**: Updates widgets in real-time based on user input  

---

## **Project Status**
- Fully functional weather app.
- Provides current and hourly forecasts.
- Can be extended with features like weather maps, notifications, or multiple locations.

---

## **About This Project**
Created in **2023**, this project demonstrates Android development skills including API integration, asynchronous programming, dynamic UI updates, and working with JSON data.  

---

## **Skills Demonstrated**
- API integration (OpenWeatherMap)
- JSON parsing and data handling
- Asynchronous background tasks (`AsyncTask`)
- Interactive UI with `SeekBar` and input widgets
- Dynamic image updates based on real-time data
- Time conversion for API timestamps
