package cn.hiio.mall.cheap.common.config

import cn.hiio.mall.cheap.common.interceptor.UserAuthInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport


/**
 * 接口拦截器
 * */
@Configuration
class ServerConfig : WebMvcConfigurationSupport() {

    @Bean
    fun getUserAuthInterceptor(): HandlerInterceptor {
        return UserAuthInterceptor()
    }

    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun responseBodyConverter(): HttpMessageConverter<String> {
        return StringHttpMessageConverter(Charsets.UTF_8)
    }
    //配置请求拦截器
    override fun addInterceptors(registry: InterceptorRegistry) {
        //增加其他的代码会出现跨域问题，TODO 待研究
        registry.addInterceptor(getUserAuthInterceptor()).addPathPatterns("/**")
    }

    //  以下两个方法是设置 请求编码的问题
    //使用 application.properties 配置编码发现不生效，最后使用以下方式配置编码
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(responseBodyConverter())
        //此处有坑，需要添加
        addDefaultHttpMessageConverters(converters)
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.favorPathExtension(false)
    }

}