# Spring Cloud

spring cloud 为开发人员提供了快速构建分布式系统的一些工具，包括配置管理、服务发现、断路器、路由、微代理、事件总线、全局锁、决策竞选、分布式会话等等。它运行环境简单，可以在开发人员的电脑上跑。

# Spring Cloud Eureka

## 一、创建服务注册中心

##### 2.1 首先创建一个Maven父工程

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
		 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.xgang.cloud</groupId>
	<artifactId>spring-cloud-learning</artifactId>
	<version>1.0-SNAPSHOT</version>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.3.1.RELEASE</version>
		<relativePath/>
	</parent>

	<modules>
		<module>eureka-server</module>
		<module>eureka-client-01</module>
	</modules>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
		<spring-cloud.version>Hoxton.SR3</spring-cloud.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>${spring-cloud.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

##### 2.2 创建2个model（SpringBoot）工程

一个eureka-server作为服务注册中心，另一个eureka-client-01作为服务提供者。

###### 2.2.1 创建eureka-server

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<artifactId>eureka-server</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>eureka-server</name>
	<packaging>jar</packaging>

	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

启动一个服务注册中心，只需要一个注解@EnableEurekaServer。

```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
```

eureka server的配置文件appication.yml。

```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  instance:
    hostname: localhost
  client:
  # 通过registerWithEureka：false和fetchRegistry：false来表明自己是一个eureka server
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

## 二、 创建服务提供者

创建SpringBoot项目，名称为eureka-client-01。pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<artifactId>eureka-client-01</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>eureka-client-01</name>
	<packaging>jar</packaging>

	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

通过注解@EnableEurekaClient 表明自己是一个eurekaclient。

```java
@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaClient01Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClient01Application.class, args);
	}

	@Value("${server.port}")
	String port;

	@Value("${spring.application.name}")
	String serverName;

	@GetMapping("/hi")
	public String home(@RequestParam(value = "name", defaultValue = "xgang") String name) {
		return "hi " + name + ", i am from " + serverName + " port : " + port;
	}
}
```

配置文件application.yml

```yaml
server:
  port: 8762

spring:
  application:
    name: eureka-client-01

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

# Spring Cloud Consul

## 一、准备工作

使用docker部署consul，启动命令如下:

```shell
docker run -d  -p 8500:8500/tcp --name consul  consul agent -server -ui -bootstrap-expect=1 -client=0.0.0.0
```

## 二、构建服务提供者

创建service-consul项目，pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<artifactId>service-consul</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>service-consul</name>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-consul-discovery</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

在工程的配置文件application.yml做下以下配置：

```yaml
server:
  port: 8766
spring:
  application:
    name: service-consul
  cloud:
    consul:
      host: 192.168.140.120
      port: 8500
```

在程序启动类加上@EnableDiscoveryClient注解，开启服务发现的功能。

```java
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceConsulApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceConsulApplication.class, args);
	}
}
```

## 三、使用Spring Cloud Consul Config来做服务配置中心

首先在工程的pom文件加上consul-config的起步依赖，代码如下：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-config</artifactId>
</dependency>
```

然后在配置文件application.yml加上以下的以下的配置，配置如下：

```yaml
spring:
  profiles:
    active: dev 
```

上面的配置指定了SpringBoot启动时的读取的profiles为dev。 然后再工程的启动配置文件bootstrap.yml文件中配置以下的配置：

```yaml
spring:
  application:
    name: service-consul
  profiles:
    active: dev
  cloud:
    consul:
      host: 192.168.140.120
      port: 8500
      config:
        enabled: true
        # 设置配置的值的格式
        format: yaml
        # 设置配的基本目录
        prefix: config
        # profiles配置分隔符,默认为‘,’
        profile-separator: ':'
        # date-key为应用配置的key名字，值为整个应用配置的字符串
        data-key: data
```

网页上访问consul的KV存储的管理界面，即http://localhost:8500/ui/dc1/kv，创建一条记录，

key值为：config/consul-provider:dev/data value值如下:

```yaml
server:
  port: 8777
foo: 你好
```

## 四、动态刷新配置

当使用spring cloud config作为配置中心的时候，可以使用spring cloud config bus支持动态刷新配置。Spring Cloud Comsul Config默认就支持动态刷新，只需要在需要动态刷新的类上加上@RefreshScope注解即可，修改代码如下：

```java
@RefreshScope
@RestController
public class HiController {
	@Value("${foo}")
	String foo;

	@GetMapping("hi")
	public String hi() {
		return foo;
	}
}
```

# Spring Cloud alibaba nacos

Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。 是Spring Cloud A 中的服务注册发现组件，类似于Consul、Eureka，同时它又提供了分布式配置中心的功能，这点和Consul的config类似，支持热加载。

**Nacos 的关键特性包括:**

- 服务发现和服务健康监测
- 动态配置服务，带管理界面，支持丰富的配置维度。
- 动态 DNS 服务
- 服务及其元数据管理

## 一、准备工作

docker安装nacos

```shell
docker pull nacos/nacos-server
docker run --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server
```

## 二、使用Nacos服务注册和发现

使用2个服务注册到Nacos上，分别为nacos-provider和nacos-consumer。

### 构建服务提供者nacos-provider

新建一个Spring Boot项目，名为nacos-provider。pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<artifactId>nacos-provider</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>nacos-provider</name>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
			<version>0.9.0.RELEASE</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>

```

在工程的配置文件application.yml做相关的配置，配置如下：

```yaml
server:
  port: 8762
spring:
  application:
    name: nacos-provider
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.140.120:8848
```

然后在Spring Boot的启动文件NacosProviderApplication加上@EnableDiscoveryClient注解，代码如下：

```java
@EnableDiscoveryClient
@SpringBootApplication
public class NacosProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(NacosProviderApplication.class, args);
	}
}
```

### 构建服务消费者nacos-consuer

构建过程同nacos-provider。

## 三、使用FeignClient调用服务

在nacos-provider工程，写一个Controller提供API服务，代码如下：

```java
@RestController
public class ProviderController {

	@GetMapping("/hi")
	public String hi(@RequestParam(value = "name", defaultValue = "xgang", required = false) String name) {
		return "hi, " + name;
	}
}
```

在nacos-consumer的pom文件引入以下的依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

在NacosConsumerApplication启动文件上加上@EnableFeignClients注解开启FeignClient的功能。

```java
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class NacosConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NacosConsumerApplication.class, args);
	}
}
```

写一个FeignClient，调用nacos-provider的服务，代码如下：

```java
@FeignClient("nacos-provider")
public interface ProviderClient {
	@GetMapping("/hi")
	String hi(@RequestParam(value = "name", defaultValue = "xgang", required = false) String name);
}

```

写一个消费API，该API使用ProviderClient来调用nacos-provider的API服务，代码如下：

```java
@RestController
public class ConsumerController {
	@Autowired
	ProviderClient providerClient;

	@GetMapping("/hi-feign")
	public String hi(@RequestParam String name) {
		return providerClient.hi(name);
	}
}
```

## 四、使用nacos作为配置中心

在nacos-provider工程上改造,在工程的pom文件引入nacos-config的Spring cloud依赖，代码如下：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-alibaba-nacos-config</artifactId>
    <version>0.9.0.RELEASE</version>
</dependency>
```

在bootstrap.yml文件配置以下内容：

```yaml
spring:
  application:
    name: nacos-provider
  profiles:
    active: dev
  cloud:
    nacos:
      config:
        server-addr: 192.168.140.120:8848
        file-extension: yml
        # prefix 默认为 spring.application.name 的值，也可以通过配置项 spring.cloud.nacos.config.prefix来配置。
        prefix: nacos-provider
```

写一个Controller,在Controller上添加 @RefreshScope 实现配置的热加载。代码如下：

```java
@RestController
@RefreshScope
public class ConfigController {

	@Value("${foo}")
	String foo;

	@GetMapping("/foo")
	public String get() {
		return foo;
	}
}
```

# Spring Cloud Ribbon

在微服务架构中，业务都会被拆分成一个独立的服务，服务与服务的通讯是基于HTTP RESTful的。Spring cloud有两种服务调用方式，一种是Ribbon+RestTemplate，另一种是feign。在这一篇文章首先讲解下基于Ribbon+RestTemplate。

Ribbon是一个负载均衡客户端，可以很好的控制HTTP和TCP的一些行为。Feign默认集成了Ribbon。

## 一、准备工作

启动eureka-server工程端口为8761；启动两个eureka-client-01实例端口分别为8762，8763。

## 二、创建一个服务消费者

##### 创建service-ribbon项目

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>
	<artifactId>service-ribbon</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>service-ribbon</name>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

配置文件application.yml

```yaml
server:
  port: 8764

spring:
  application:
    name: service-ribbon

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

在工程的启动类中,通过@EnableDiscoveryClient向服务中心注册；并且向程序的ioc注入一个bean: RestTemplate;并通过@LoadBalanced注解表明这个RestRemplate开启负载均衡的功能。

```java
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRibbonApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
```

写一个测试类HelloService，通过之前注入ioc容器的restTemplate来消费service-client-01服务的“/hi”接口，在这里我们直接用的程序名替代了具体的url地址，在Ribbon中它会根据服务名来选择具体的服务实例，根据服务实例在请求的时候会用具体的url替换掉服务名，代码如下：

```java
@Service
public class HelloService {
	@Autowired
	RestTemplate restTemplate;

	public String hiService(String name) {
		return restTemplate.getForObject("http://EUREKA-CLIENT-01/hi?name=" + name, String.class);
	}
}
```

写一个controller，在controller中用调用HelloService 的方法，代码如下：

```java
@RestController
public class HelloController {

	@Autowired
	HelloService helloService;

	@GetMapping("/hi")
	public String hi(@RequestParam String name) {
		return helloService.hiService(name);
	}
}
```

# Spring Cloud Feign

Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。

简而言之：

- Feign 采用的是基于接口的注解
- Feign 整合了ribbon，具有负载均衡的能力
- 整合了Hystrix，具有熔断的能力

## 一、准备工作

启动eureka-server工程端口为8761；启动两个eureka-client-01实例端口分别为8762，8763。

## 二、创建一个Feign服务

##### 2.1 创建service-feign项目

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<artifactId>service-feign</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>service-feign</name>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

配置文件application.yml

```yaml
server:
  port: 8765

spring:
  application:
    name: service-feign

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

在程序的启动类ServiceFeignApplication ，加上@EnableFeignClients注解开启Feign的功能

```java
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ServiceFeignApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceFeignApplication.class, args);
	}
}
```

定义一个feign接口，通过@ FeignClient（“服务名”），来指定调用哪个服务。代码如下：

```java
@FeignClient(value = "EUREKA-CLIENT-01")
public interface EurekaClient {
	@GetMapping(value = "/hi")
	String sayHiFromClient(@RequestParam(value = "name") String name);
}
```

controller层代码如下：

```java
@RestController
public class HelloController {

	@Autowired
	EurekaClient eurekaClient;

	@GetMapping("/hi")
	public String hi(@RequestParam String name) {
		return eurekaClient.sayHiFromClient(name);
	}
}
```

# Spring Cloud Hystrix

在微服务架构中，根据业务来拆分成一个个的服务，服务与服务之间可以相互调用（RPC），在Spring Cloud可以用RestTemplate+Ribbon和Feign来调用。为了保证其高可用，单个服务通常会集群部署。由于网络原因或者自身的原因，服务并不能保证100%可用，如果单个服务出现问题，调用这个服务就会出现线程阻塞，此时若有大量的请求涌入，Servlet容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。

## 一、准备工作

启动eureka-server工程；启动service-client-01工程。

## 二、在Ribbon使用断路器hystrix

改造serice-ribbon 工程的代码，首先在pox.xml文件中加入spring-cloud-starter-netflix-hystrix的起步依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

在程序的启动类ServiceRibbonApplication 加@EnableHystrix注解开启Hystrix：

```java
@EnableHystrix
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceRibbonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRibbonApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
```

改造HelloService类，在hiService方法上加上@HystrixCommand注解。该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法，熔断方法直接返回了一个字符串。代码如下：

```java
@Service
public class HelloService {
	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "error")
	public String hiService(String name) {
		return restTemplate.getForObject("http://EUREKA-CLIENT-01/hi?name=" + name, String.class);
	}

	public String error(String name) {
		return "hi, " + name + ", sorry, error!";
	}
}

```

## 三、Feign中使用断路器

Feign是自带断路器的，它没有默认打开。需要在配置文件中配置打开它，在配置文件加以下代码：

```yaml
feign:
  hystrix:
    enabled: true
```

基于service-feign工程进行改造，只需要在FeignClient的EurekaClient接口的注解中加上fallback的指定类就行了：

```java
@FeignClient(value = "EUREKA-CLIENT-01", fallback = EurekaClientHystrix.class)
public interface EurekaClient {

	@GetMapping(value = "/hi")
	String sayHiFromClient(@RequestParam(value = "name") String name);
}
```

EurekaClientHystrix需要实现EurekaClient接口，并注入到Ioc容器中，代码如下：

```java
@Component
public class EurekaClientHystrix implements EurekaClient {
	@Override
	public String sayHiFromClient(String name) {
		return "sorry " + name;
	}
}
```

# Spring Cloud alibaba Sentinel

Sentinel，中文翻译为哨兵，是为微服务提供**流量控制**、**熔断降级**的功能，它和Hystrix提供的功能一样，可以有效的解决微服务调用产生的“雪崩”效应，为微服务系统提供了稳定性的解决方案。随着Hytrxi进入了维护期，不再提供新功能，Sentinel是一个不错的替代方案。通常情况，Hystrix采用线程池对服务的调用进行隔离，Sentinel才用了用户线程对接口进行隔离，二者相比，Hystrxi是服务级别的隔离，Sentinel提供了接口级别的隔离，Sentinel隔离级别更加精细，另外Sentinel直接使用用户线程进行限制，相比Hystrix的线程池隔离，减少了线程切换的开销。另外Sentinel的DashBoard提供了在线更改限流规则的配置，也更加的优化。

## 一、准备工作

Sentinel 控制台提供一个轻量级的控制台，它提供机器发现、单机资源实时监控、集群资源汇总，以及规则管理的功能. Sentinel DashBoard下载地址：https://github.com/alibaba/Sentinel/releases

下载完成后，以下的命令启动

```shell
java -jar sentinel-dashboard-1.7.2.jar
```

默认启动端口为8080，可以-Dserver.port=8081的形式改变默认端口。启动成功后，在浏览器上访问localhost:8080，就可以显示Sentinel的登陆界面，登陆名为sentinel，密码为sentinel。

## 二、改造nacos-provider项目

Sentinel作为Spring Cloud Alibaba的组件之一，在Spring Cloud项目中使用它非常的简单。现在以案例的形式来讲解如何在Spring Cloud项目中使用Sentinel。在工程的pom文件加上sentinel的Spring Cloud起步依赖，代码如下：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    <version>0.9.0.RELEASE</version>
</dependency>
```

在工程的配置文件application.yml文件中配置，需要新增2个配置：

- spring.cloud.sentinel.transport.port: 8719 ，这个端口配置会在应用对应的机器上启动一个 Http Server，该 Server 会与 Sentinel 控制台做交互。比如 Sentinel 控制台添加了1个限流规则，会把规则数据 push 给这个 Http Server 接收，Http Server 再将规则注册到 Sentinel 中。
- spring.cloud.sentinel.transport.dashboard: 8080，这个是Sentinel DashBoard的地址。

```yaml
spring:
    sentinel:
      transport:
        port: 8719
        dashboard: localhost:8081
```

写一个Controller，在接口上加上SentinelResource注解就可以了。

```java
@RestController
public class ProviderController {
	@GetMapping("/hi")
	@SentinelResource(value = "hi")
	public String hi(@RequestParam(value = "name", defaultValue = "xgang", required = false) String name) {
		return "hi, " + name;
	}
}
```

关于@SentinelResource 注解，有以下的属性：

- value：资源名称，必需项（不能为空）
- entryType：entry 类型，可选项（默认为 EntryType.OUT）
- blockHandler / blockHandlerClass: blockHandler 对应处理 BlockException 的函数名称，可选项
- fallback：fallback 函数名称，可选项，用于在抛出异常的时候提供 fallback 处理逻辑。

## 三、在FeignClient中使用Sentinel

Hystrix默认集成在Spring Cloud 的Feign Client组件中，Sentinel也可以提供这样的功能。现以案例的形式来讲解如何在FeignClient中使用Sentinel，z本案例是在之前的nacos教程案例的nacos-consumer工程上进行改造，除了引入spring-cloud-starter-alibaba-sentinel，还需要引入spring-cloud-starter-openfeign，代码如下：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    <version>0.9.0.RELEASE</version>
</dependency>
```

在配置文件中需要加上sentinel.transport. dashboard配置外，还需要加上feign.sentinel.enabled的配置，代码如下：

```yaml
feign:
  sentinel:
    enable: true
```

# Spring Cloud Gateway

Spring Cloud Gateway是Spring Cloud官方推出的第二代网关框架，取代Zuul网关。网关作为流量的，在微服务系统中有着非常作用，网关常见的功能有路由转发、权限校验、限流控制等作用。

## 一、创建gateway项目

###### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<artifactId>gateway</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>gateway</name>

	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

###### 配置文件

```yaml
server:
  port: 8080
spring:
  application:
    name: gateway
  cloud:
    gateway:
      routes:
        - id: eureka-client-01
          uri: lb://eureka-client-01
          predicates:
            - Path=/**
      discovery:
        locator:
          lower-case-service-id: true
          enabled: true

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## 二、Redis限流

pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
```

配置文件application.yml

```yaml
server:
  port: 8080
spring:
  application:
    name: gateway
  cloud:
    gateway:
      discovery:
        locator:
          lower-case-service-id: true
          enabled: true
      routes:
        - id: eureka-client-01
          uri: lb://eureka-client-01
          predicates:
            - Path=/**
          filters:
            - name: RequestRateLimiter
              args:
                key-resolver: '#{@hostAddrKeyResolver}'
                redis-rate-limiter.replenishRate: 1
                redis-rate-limiter.burstCapacity: 3
  redis:
    host: 192.168.140.120
    port: 6379
    database: 0

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

创建HostAddrKeyResolver实现KeyResolver接口

```java
public class HostAddrKeyResolver implements KeyResolver {
	@Override
	public Mono<String> resolve(ServerWebExchange exchange) {
        // 根据uri限流
		return Mono.just(exchange.getRequest().getURI().getPath());
	}
}
```

交给ioc

```java
@Bean
public HostAddrKeyResolver hostAddrKeyResolver() {
    return new HostAddrKeyResolver();
}
```

# Spring Cloud Config

在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。

## 一、构建config-server项目

创建一个spring-boot项目，取名为config-server,其pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>com.xgang.cloud</groupId>
		<artifactId>spring-cloud-learning</artifactId>
		<version>1.0-SNAPSHOT</version>
	</parent>

	<artifactId>config-server</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>config-server</name>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>

```

在程序的入口Application类加上@EnableConfigServer注解开启配置服务器的功能，代码如下：

```java
@EnableEurekaClient
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
```

需要在程序的配置文件application.yml文件配置以下：

```yaml
server:
  port: 8888

spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/244354285/spring-cloud-config/
          # 包路径
          search-paths: config
      label: master

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## 二、改造使用配置eureka-client-01项目

pom.xml文件中添加如下依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

配置文件bootstrap.yml

```yaml
spring:
  application:
    name: eureka-client-01
  cloud:
    config:
      label: master
      profile: dev
      uri: http://localhost:8888/
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## 三、从数据库读取配置

#### 改造config-server工程

创建工程config-server，在工程的pom文件引入config-server的起步依赖，mysql的连接器，jdbc的起步依赖，代码如下:

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

在工程的配置文件application.yml下做以下的配置：

```yaml
server:
  port: 8888

spring:
  application:
    name: config-server
  profiles:
    active: jdbc
  cloud:
    config:
      label: master
      server:
        jdbc:
          sql: SELECT key1, value1 from config_properties where APPLICATION=? and PROFILE=? and LABEL=?
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/config-jdbc?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&serverTimezone=GMT%2B8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

在程序的启动文件ConfigServerApplication加上@EnableConfigServer注解，开启ConfigServer的功能。

#### 初始化数据库

由于Config-server需要从数据库中读取，所以读者需要先安装MySQL数据库，安装成功后，创建config-jdbc数据库，数据库编码为utf-8，然后在config-jdbc数据库下，执行以下的数据库脚本：

```sql
CREATE TABLE `config_properties` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key1` varchar(50) COLLATE utf8_bin NOT NULL,
  `value1` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `application` varchar(50) COLLATE utf8_bin NOT NULL,
  `profile` varchar(50) COLLATE utf8_bin NOT NULL,
  `label` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
```

其中key1字段为配置的key,value1字段为配置的值，application字段对应于应用名，profile对应于环境，label对应于读取的分支，一般为master。

插入数据config-client 的2条数据，包括server.port和foo两个配置，具体数据库脚本如下:

```sql
insert into `config_properties` (`id`, `key1`, `value1`, `application`, `profile`, `label`) values('1','server.port','8083','eureka-client-01','dev','master');
insert into `config_properties` (`id`, `key1`, `value1`, `application`, `profile`, `label`) values('2','foo','bar-jdbc','eureka-client-01','dev','master');
```

#### 改造eureka-client-01项目，读取配置

在程序的启动配置文件 bootstrap.yml做程序的相关配置，一定要是bootstrap.yml，不可以是application.yml，bootstrap.yml的读取优先级更高，配置如下：

```yaml
spring:
  application:
    name: eureka-client-01
  cloud:
    config:
      profile: dev
      uri: http://localhost:8888/
      fail-fast: true
```

# Spring Cloud Bus

Spring Cloud Bus 将分布式的节点用轻量的消息代理连接起来。它可以用于广播配置文件的更改或者服务之间的通讯，也可以用于监控。本文要讲述的是用Spring Cloud Bus实现通知微服务架构的配置文件的更改。

## 一、改造使用配置eureka-client-01项目

在pom文件加上依赖，完整的配置文件如下：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

在配置文件application.yml中加上RabbitMq的配置，包括RabbitMq的地址、端口，用户名、密码。并需要加上spring.cloud.bus的三个配置，具体如下：

```yaml
server:
  port: 8763
spring:
  application:
    name: eureka-client-01
  rabbitmq:
    host: 192.168.140.120
    port: 5672
    username: guest
    password: guest
  cloud:
    bus:
      enbale: true
      trace:
        enable: true
management:
  endpoints:
    web:
      exposure:
        include: bus-refresh
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

启动类加上注解@RefreshScope：

```java
@SpringBootApplication
@EnableEurekaClient
@RestController
@RefreshScope
@EnableDiscoveryClient
public class EurekaClient01Application {
	public static void main(String[] args) {
		SpringApplication.run(EurekaClient01Application.class, args);
	}
}
```

现在改变配置文件的值，只需要发送post请求：http://localhost:8673/actuator/bus-refresh

# Spring Cloud Sleuth（服务追踪）

## 一、构建server-zipkin

下载完成jar 包之后，需要运行jar，如下：

```shell
java -jar zipkin-server-2.10.1-exec.jar
```

## 二、改造eureka-client-01和service-feign项目

在其pom引入起步依赖spring-cloud-starter-zipkin，代码如下：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

在其配置文件application.yml指定zipkin server的地址，头通过配置“spring.zipkin.base-url”指定：

```yaml
spring:
  zipkin:
    base-url: http://localhost:9411
```

依次启动上面的工程，打开浏览器访问：http://localhost:9411/