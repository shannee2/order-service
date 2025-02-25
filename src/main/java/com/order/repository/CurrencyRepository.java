//package com.order.repository;
//
//import com.order.model.money.Currency;
//import com.order.model.money.CurrencyType;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface CurrencyRepository extends JpaRepository<Currency, Long> {
//    Optional<Currency> findByType(CurrencyType type);
//}