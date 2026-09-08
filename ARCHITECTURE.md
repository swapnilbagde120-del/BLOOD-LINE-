# BLOOD BANK — Production Architecture

## Android
- Kotlin + Jetpack Compose + Material 3
- Single activity
- Permission-aware location/notification features

## Backend
- OTP authentication and role-based access
- Verified hospitals, blood centers and donors
- PostgreSQL/Supabase or Firebase
- Realtime emergency request/status updates
- Google Maps or Mapbox routing
- Push notifications
- Encrypted sensitive documents
- Audit logs, rate limiting and duplicate detection
- Authorized blood-stock source with last-verified timestamp

## AI MODE
The app includes a ChatGPT-style conversation UI. The starter uses demo responses.
For a real AI assistant, connect a secure server-side AI integration. Never place secret AI API keys inside the Android APK.

AI can assist with app navigation, FAQs and general information. It must not make clinical, transfusion compatibility or emergency-treatment decisions.

## Safety
Do not invent live blood stock, hospital verification, GPS or ETA.
Donation is not arbitrary consumer sale; show only official service/processing/logistics charges where applicable.
Preserve traceability and unique identifiers for critical blood workflows.
