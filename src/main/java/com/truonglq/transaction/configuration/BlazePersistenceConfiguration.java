//package com.truonglq.transaction.configuration;
//
//import com.blazebit.persistence.Criteria;
//import com.blazebit.persistence.CriteriaBuilderFactory;
//import jakarta.persistence.EntityManagerFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class BlazePersistenceConfiguration {
//
//    private final EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    CriteriaBuilderFactory criteriaBuilderFactory() {
//        return Criteria.getDefault().createCriteriaBuilderFactory(entityManagerFactory);
//    }
//
//}
