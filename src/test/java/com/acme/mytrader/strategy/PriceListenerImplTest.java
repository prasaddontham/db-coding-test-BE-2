package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListenerImpl;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//test class test class
public class PriceListenerImplTest {

  @Test
  public void testPriceListenerImpl() {
    ExecutionService executionService = Mockito.mock(ExecutionService.class);

    PriceListenerImpl priceListenerImpl = new PriceListenerImpl("IBM", 45.00, 150,executionService,
        false);

    assertThat(priceListenerImpl.getSecurity()).isEqualTo("IBM");
    assertThat(priceListenerImpl.getTriggerLevel()).isEqualTo(45.00);
    assertThat(priceListenerImpl.getQuantityToPurchase()).isEqualTo(150);
    assertThat(priceListenerImpl.isExecutedOrNot()).isFalse();
  }

  @Test
  public void whenThresholdIsMet() {
    ExecutionService executionService = Mockito.mock(ExecutionService.class);
    ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
    ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

    PriceListenerImpl priceListenerImpl = new PriceListenerImpl("IBM", 50.00, 100,executionService,
        false);
    priceListenerImpl.priceUpdate("IBM", 25.00);

    verify(executionService, times(1))
        .buy(acString.capture(), acDouble.capture(), acInteger.capture());

  }

  @Test
  public void whenThresholdIsNotMet() {
    ExecutionService executionService = Mockito.mock(ExecutionService.class);
    ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
    ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

    PriceListenerImpl priceListenerImpl = new PriceListenerImpl("IBM", 60.00, 100,
            executionService,false);
    priceListenerImpl.priceUpdate("IBM", 65.00);

    verify(executionService, times(0))
        .buy(acString.capture(), acDouble.capture(), acInteger.capture());
    assertThat(priceListenerImpl.isExecutedOrNot())
        .as("Should be the trade is not successfully executed").isFalse();
  }
}
