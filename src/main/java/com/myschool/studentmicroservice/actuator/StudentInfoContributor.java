package com.myschool.studentmicroservice.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class StudentInfoContributor implements InfoContributor {
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("version", "1.0")
               .withDetail("author", "Your Name")
               .withDetail("database", "MongoDB");
    }
}

