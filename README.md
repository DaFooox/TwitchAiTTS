# TwitchAiTTS

## Overview

Enable viewers to interact with your Twitch stream in a fun and engaging way by using channel points redemptions to trigger realistic AI-generated text-to-speech powered by ElevenLabs.
Features

- Integration with Twitch channel point redemptions
- Seamless connection to the ElevenLabs API
- Customizable AI voice options provided by ElevenLabs
- Flexible text input for diverse speech synthesis
- User-friendly configuration settings

## Prerequisites:

- A Twitch account
- An ElevenLabs account (with API key)

## Installation:

Clone the repository: git clone [https://github.com/<your-username>/<project-name>.git](https://github.com/DaFooox/TwitchAiTTS.git)
Change the following variables in the main.kt
TWITCH_CLIENT_ID=your_twitch_client_id
TWITCH_CLIENT_SECRET=your_twitch_client_secret
ELEVENLABS_API_KEY=your_elevenlabs_api_key
## Configuration:

Set up a channel point redemption for the AI TTS feature on your Twitch channel.
Modify the configuration file to adjust voice options, rate limits, cooldowns, etc.

## Usage

- Viewers accumulate channel points on your Twitch stream.
- Viewers redeem channel points for the AI TTS reward.
- Viewers provide text input.
- The application sends the text to ElevenLabs and the synthesized speech is played on your stream.


## Technology Stack

List core technologies:
- Twitch API
- ElevenLabs API
- WebSockets
- OkHttp3
- JSON

## Contributing
We welcome contributions to this project! Please follow the following guidelines:

- Reporting Issues: Open an issue with a clear description of the bug or problem.
- Suggesting Features: Describe the new feature and potential use cases.
- Submitting Code Changes: Fork the repo, create a feature branch, and submit a pull request.


## Important considerations:
### Set responsible rate limits and cooldowns for channel point redemptions to avoid excessive API usage and costs.
