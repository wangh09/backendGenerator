spring.application.name=api-gateway
server.port=2223
ribbon.eureka.enabled=false

${urlmappings}
spring.datasource.url=jdbc:mysql://${dbHost}:${dbPort}/${dbName}
spring.datasource.username=${dbUser}
spring.datasource.password=${dbPass}
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.hikari.maxLifetime= 1765000
spring.datasource.hikari.maximumPoolSize= 15

mybatis.mapperLocations=classpath:mapper/*.xml
mybatis.typeAliasesPackage=com.bean

springfox.documentation.swagger.v2.path: /api-docs