# JAVA - Text-to-speech code test

Code example using both TTS `Gcloud` and `AWS Polly` services

## Requirements

Each variant of the code have a certain file requirement for the execution.

For `GCloud` example, an account service with the privilege for TTS usage is required and the path can be specified in the example code.

For `AWS` example, a credential file located in `~/.aws/credentials` is required with a default scope, the api key must have access to `Polly` in order to run.

## Usage

```bash
# Execute GCloud code example
$ ./gradlew runTTSGcloud

# Execute AWS code example
$ ./gradlew runTTSAWS
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)