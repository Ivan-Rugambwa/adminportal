package io.camunda.connector;

import static io.camunda.connector.SaveDataFunction.updateSeatReport;
import static io.camunda.connector.SaveDataFunction.login;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.ExecutionException;

public class MyRequestTest {


}