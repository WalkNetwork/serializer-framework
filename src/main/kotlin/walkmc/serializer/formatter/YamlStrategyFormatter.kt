package walkmc.serializer.formatter

import kotlinx.serialization.*
import walkmc.serializer.*
import walkmc.serializer.strategy.*
import java.io.*

class YamlStrategyFormatter(
   override val model: YamlFormat,
   encoder: EncoderStrategy,
   decoder: DecoderStrategy
) : StrategyFormatter(model, encoder, decoder), YamlFormat by model {
   override fun <T> encodeTo(output: OutputStream, serializer: SerializationStrategy<T>, value: T) {
      model.encodeTo(output, DefaultSerialEncoder(encoder, serializer), value)
   }
   
   override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
      return model.encodeToString(DefaultSerialEncoder(encoder, serializer), value)
   }
   
   override fun <T> decodeFrom(input: InputStream, deserializer: DeserializationStrategy<T>): T {
      return model.decodeFrom(input, DefaultSerialDecoder(decoder, deserializer))
   }
   
   override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
      return model.decodeFromString(DefaultSerialDecoder(decoder, deserializer), string)
   }
   
   override var serializersModule = super.serializersModule
}
