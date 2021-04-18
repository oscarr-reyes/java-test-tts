package dev.oscarreyes.tts.aws;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.*;

import java.io.InputStream;

/**
 * TTS example using Polly (TTS) AWS service
 *
 * Base code example: https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/polly/src/main/java/com/example/polly/PollyDemo.java
 *
 * AWS reference: https://sdk.amazonaws.com/java/api/latest/
 */
public class Main {
	private static String INPUT_TEXT = "Hello world, this is my first synthesized text using Polly service from AWS";

	public static void main(String[] args) {
		final PollyClient client = PollyClient.builder()
			.region(Region.US_EAST_1)
			.build();

		final InputStream outputAudio = synthesizeSpeech(client);

		playAudio(outputAudio);

		client.close();
	}

	private static InputStream synthesizeSpeech(PollyClient client) {
		final DescribeVoicesRequest voicesRequest = DescribeVoicesRequest.builder()
			.engine(Engine.STANDARD)
			.languageCode(LanguageCode.EN_US)
			.build();
		final DescribeVoicesResponse voicesResult = client.describeVoices(voicesRequest);

		if(!voicesResult.hasVoices()) {
			System.out.println("Unable to find voices");

			return null;
		}

		final Voice voice = voicesResult.voices()
			.stream()
			.filter(v -> v.idAsString().equals("Salli"))
			.findAny()
			.orElse(null);

		final SynthesizeSpeechRequest speechRequest = SynthesizeSpeechRequest.builder()
			.text(INPUT_TEXT)
			.voiceId(voice.id())
			.outputFormat(OutputFormat.MP3)
			.build();

		return client.synthesizeSpeech(speechRequest);
	}

	private static void playAudio(InputStream audio) {
		try {
			AdvancedPlayer player = new AdvancedPlayer(audio, FactoryRegistry.systemRegistry().createAudioDevice());

			player.setPlayBackListener(new PlaybackListener() {
				@Override
				public void playbackStarted(PlaybackEvent evt) {
					System.out.println("Player: Audio started");
				}

				@Override
				public void playbackFinished(PlaybackEvent evt) {
					System.out.println("Player: Audio finished");
				}
			});

			player.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
}
