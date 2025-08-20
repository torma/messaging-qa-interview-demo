package com.ecosio.messaging.task.testcase;

import com.ecosio.messaging.task.model.Contact;
import com.ecosio.messaging.task.util.HttpClientHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseTest {

  protected final String appUnderTestBaseUrl = "http://localhost:18080/contact/";

  /**
   * gets all contacts where parameter <code>firstname</code> is a substring of the contacts firstname
   *
   * @param firstname
   * @return list of all matching contacts
   * @throws IOException
   */
  protected List<Contact> getContactByFirstname(String firstname) throws IOException {

    HttpGet httpGet = new HttpGet(appUnderTestBaseUrl + "firstname/" + firstname);
    return connectionHelper(httpGet);

  }

  /**
   * gets a list of all contacts stored of the app
   *
   * @return list of all contacts
   * @throws IOException
   */
  protected List<Contact> getAllContacts() throws IOException {

    HttpGet httpGet = new HttpGet(appUnderTestBaseUrl + "allContacts");
    return connectionHelper(httpGet);

  }

  /**
   * updates an existing contact
   *
   * @param currentContact contact to be updated
   * @param updatedContact contact to update the existing one to
   */
  protected void updateContact(
      Contact currentContact,
      Contact updatedContact
  ) {
      // currentContact can be useful in case we if we would need to implement negative tests for the endpoint
      // but TBH that test should be a unit test

      HttpPost httpPost = new HttpPost(appUnderTestBaseUrl + "/createOrUpdateContact/" + currentContact.getId());

      httpPost.setEntity(pojoToJsonStringEntity(updatedContact));

      // I only added this try catch to avoid changing the signature as it was not part of the TODO comment
      // But normally I would add the IOException to the signature as I assume there is nothing really to be done in that case.
      try {
          connectionHelper(httpPost);
      } catch (IOException ignored) {
      }

      // At this point I would actually return either the contract from the update or the response object produced by httpClient.execute
  }

  /**
   * connection helper to abstract the "connection layer" from the "application layer"
   *
   * @param httpRequestBase
   * @return list of contacts based on the request
   * @throws IOException
   */
  private List<Contact> connectionHelper(HttpRequestBase httpRequestBase) throws IOException {

    try (CloseableHttpClient httpClient = HttpClientHelper.getHttpClientAcceptsAllSslCerts()) {
      try {

        ObjectMapper mapper = new ObjectMapper();
        String response = IOUtils.toString(
            httpClient.execute(httpRequestBase)
                .getEntity()
                .getContent(),
            StandardCharsets.UTF_8
        );
        List<Contact> contacts = mapper.readValue(
            response,
            new TypeReference<List<Contact>>() {}
        );
        return contacts;

      } finally {
        httpRequestBase.releaseConnection();
      }
    }

  }

  protected static StringEntity pojoToJsonStringEntity(Object pojo) {
      ObjectMapper objectMapper = new ObjectMapper();
      String requestBody;
      try {
          requestBody = objectMapper.writeValueAsString(pojo);
      } catch (JsonProcessingException e) {
          // My assumption is that we will never actually get here.
          // TBH this exception on the writeValueAsString signature looks BS
          // Also see discussion https://stackoverflow.com/questions/26716020/how-to-get-a-jsonprocessingexception-using-jackson
          log.error("Unexpected exception when trying to write json object {}", pojo, e);
          throw new RuntimeException();
      }
      return new StringEntity(requestBody, ContentType.APPLICATION_JSON);
  }
}
