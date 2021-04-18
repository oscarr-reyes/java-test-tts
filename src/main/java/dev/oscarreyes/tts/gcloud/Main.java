package dev.oscarreyes.tts.gcloud;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TTS example using TTS google cloud service
 *
 * Supported voices and languages: https://cloud.google.com/text-to-speech/docs/voices
 *
 * Base code example in python: https://codelabs.developers.google.com/codelabs/cloud-text-speech-python3
 *
 * Text-to-speech reference: https://googleapis.dev/java/google-cloud-texttospeech/latest/
 */
public class Main {
	private static String JSON_FILE = "/home/oscar/tts-key.json"; // Service account json credentials
	private static String FILE_OUT = "/home/oscar/tts-test.wav";  // Output path directory

	private static String TEXT = "Hello world";                   // Text to synthesize speech

	public static void main(String[] args) throws IOException {
		final TextToSpeechClient client = getClient();
		final ListVoicesResponse listResponse = client.listVoices(""); // Language codes to filter. Empty for all

		listResponse.getVoicesList()
			.stream()
			.filter(voice -> voice.getName().toLowerCase().contains("standard"))
			.distinct()
			.forEach(voice -> {
				final String voiceName = voice.getName();

				System.out.println(String.format("%s -> %s", voiceName, voice.getLanguageCodesList().toString()));
			});

		final SynthesisInput input = SynthesisInput.newBuilder()
			.setText(TEXT)
			.build();

		final VoiceSelectionParams voiceParams = VoiceSelectionParams.newBuilder()
			.setLanguageCode("en-US")
			.setName("en-US-Standard-A")
			.build();

		final AudioConfig audioConfig = AudioConfig.newBuilder()
			.setAudioEncodingValue(AudioEncoding.LINEAR16_VALUE)
			.build();

		final SynthesizeSpeechResponse synthesizeSpeechResponse = client.synthesizeSpeech(input, voiceParams, audioConfig);

		try (FileOutputStream out = new FileOutputStream(FILE_OUT)) {
			out.write(synthesizeSpeechResponse.getAudioContent().toByteArray());
		}
	}

	private static TextToSpeechClient getClient() throws IOException {
		final GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(JSON_FILE));

		final TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
			.setCredentialsProvider(FixedCredentialsProvider.create(credentials))
			.build();

		return TextToSpeechClient.create(settings);
	}
}
