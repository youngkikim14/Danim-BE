package com.project.danim_be.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.danim_be.post.dto.ResponseDto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisCacheConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Autowired
	private ObjectMapper objectMapper;

	//레디스 서버에 연결하는 메서드
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host,port);
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}
	@Bean
	public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance , ObjectMapper.DefaultTyping.NON_FINAL);
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		serializer.setObjectMapper(mapper);
		serializer.serialize(mapper);
		return serializer;
	}
	@Bean
	public RedisTemplate<String, PostResponseDto> postResponseDtoRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, PostResponseDto> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		// Redis에 저장되는 데이터를 Json형식으로 ,Json형식을 다시 Java형식으로바꾼다
		Jackson2JsonRedisSerializer<PostResponseDto> serializer = new Jackson2JsonRedisSerializer<>(PostResponseDto.class);


		ObjectMapper mapper = new ObjectMapper();
		//모든필드에 대해서 getter,setter메서드를 필요로 하지않고, private필드에 직접접근하도록 설정
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		//polymorphic 타입 처리를 가능하게 하는 설정입니다. 즉, superclass를 가지고 있는 객체를 serialize/deserialize할 때 사용됩니다.
		mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance , ObjectMapper.DefaultTyping.NON_FINAL);
		//LocalDatetime 의 Time Api에접근할수있도록 자바타임모듈 등록
		mapper.registerModule(new JavaTimeModule());
		// Date 를 timestamp로쓰는것을 비활성화함 >>>iso-8601형식의 String값으로 date를 출력하도록함
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		//지금까지 설정한값을 serializer에 저장함
//		serializer.setObjectMapper(mapper);
		serializer.serialize(mapper);

		//여기서부터는 위의조건을 정한값에대한 key value값을 직렬화를 하기시작함
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		//필요로하는 모든속성이 제대로되어있는지확인
		template.afterPropertiesSet();

		//이로써 PostResponseDto 객체를 Json형태로 저장하거나 꺼내올때 PostResponseDto객체로변환이가능하게함
		return template;
	}
	//ChatCache
	@Bean
	public RedisTemplate<?, ?> chatRedisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		// ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

		// Jackson2JsonRedisSerializer 설정
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		jackson2JsonRedisSerializer.serialize(objectMapper);
//		setObjectMapper(objectMapper);

		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, String> RefreshTokenRedisTemplate() {
		// redisTemplate를 받아와서 set, get, delete를 사용
		RedisTemplate<String, String> refreshRedisTemplate = new RedisTemplate<>();
		// setKeySerializer, setValueSerializer 설정
		// redis-cli을 통해 직접 데이터를 조회 시 알아볼 수 없는 형태로 출력되는 것을 방지
		refreshRedisTemplate.setKeySerializer(new StringRedisSerializer());
		refreshRedisTemplate.setValueSerializer(new StringRedisSerializer());
		refreshRedisTemplate.setConnectionFactory(redisConnectionFactory());

		return refreshRedisTemplate;
	}

}










