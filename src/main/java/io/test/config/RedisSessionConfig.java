package io.test.config;

import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
@RequiredArgsConstructor
//@EnableRedisRepositories
public class RedisSessionConfig {

//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }
//
//    /*
//     * redis-cli config set notify-keyspace-events Egx
//     */
//    @Bean
//    public ConfigureRedisAction configureRedisAction() {
//        return ConfigureRedisAction.NO_OP;
//    }

//    @Order(Ordered.LOWEST_PRECEDENCE)
//    @Bean
//    public RedisServerBean redisServer() {
//        return new RedisServerBean();
//    }

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(
//          "localhost", 
//          6370);
//    }

//    @Bean
//    public SessionRegistry sessionRegistry(RedisIndexedSessionRepository sessionRepository) {
//        sessionRepository.setDefaultMaxInactiveInterval(1800);
//        sessionRepository.setFlushMode(FlushMode.IMMEDIATE);
//        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
//    }

//    class RedisServerBean implements InitializingBean, DisposableBean {
//        private RedisServer redisServer;
//
//        public void afterPropertiesSet() throws Exception {
//            redisServer = new RedisServer(6370);
//            redisServer.start();
//        }
//
//        public void destroy() throws Exception {
//            if (redisServer != null) {
//                redisServer.stop();
//            }
//        }
//    }
}
