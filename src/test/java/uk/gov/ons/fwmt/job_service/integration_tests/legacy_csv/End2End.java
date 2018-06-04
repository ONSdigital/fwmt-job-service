package uk.gov.ons.fwmt.job_service.integration_tests.legacy_csv;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import cucumber.api.java.en.Then;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;

@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/cucumber/End2End.feature"}, glue = {
    "uk.gov.ons.fwmt.legacy_gateway.integration_tests.legacy_csv"})
public class End2End {
  
  private String[] usernames;

  @Given("^two usernames$")
  public void two_usernames(DataTable arg1) throws Exception {
    
  }

  @Given("^auth_nos$")
  public void auth_nos(DataTable arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
    // E,K,V must be a scalar (String, Integer, Date, enum etc)

  }

  @Then("^create the users in the tm_jobs table$")
  public void create_the_users_in_the_tm_jobs_table() throws Exception {

  }

  @Given("^the 'sample_GFF_(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+)Z\\.csv' \\.csv is posted to the '/samples' endpoint$")
  public void the_sample_GFF__T_Z_csv_csv_is_posted_to_the_samples_endpoint(int arg1, int arg2, int arg3, int arg4,
      int arg5, int arg6) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @When("^the sample service returns HTTP status code (\\d+)$")
  public void the_sample_service_returns_HTTP_status_code(int arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Then("^the tm_jobs table contains the tm_job_id 'NEWJOB(\\d+)-(\\d+)-(\\d+)'$")
  public void the_tm_jobs_table_contains_the_tm_job_id_NEWJOB(int arg1, int arg2, int arg3) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Then("^the row has last_auth_no 'INT(\\d+)'$")
  public void the_row_has_last_auth_no_INT(int arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Then("^the tm_jobs table contains the tm_job_id 'REALLOC(\\d+)-(\\d+)-(\\d+)'$")
  public void the_tm_jobs_table_contains_the_tm_job_id_REALLOC(int arg1, int arg2, int arg3) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Then("^the tm_jobs table contains the tm_job_id 'REISS(\\d+)-(\\d+)-(\\d+)'$")
  public void the_tm_jobs_table_contains_the_tm_job_id_REISS(int arg1, int arg2, int arg3) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Given("^the 'sample_LFS_(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+)Z\\.csv' \\.csv is posted to the '/samples' endpoint$")
  public void the_sample_LFS__T_Z_csv_csv_is_posted_to_the_samples_endpoint(int arg1, int arg2, int arg3, int arg4,
      int arg5, int arg6) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Then("^the tm_jobs table contains the tm_job_id 'NEWJOB(\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) - (\\d+)K'$")
  public void the_tm_jobs_table_contains_the_tm_job_id_NEWJOB_K(int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, int arg8, int arg9) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Then("^the tm_jobs table contains the tm_job_id 'REALLOC(\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) - (\\d+)K'$")
  public void the_tm_jobs_table_contains_the_tm_job_id_REALLOC_K(int arg1, int arg2, int arg3, int arg4, int arg5,
      int arg6, int arg7, int arg8, int arg9) throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Given("^tm_job_ids array$")
  public void tm_job_ids_array(DataTable arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
    // E,K,V must be a scalar (String, Integer, Date, enum etc)
  }

  @When("^this array has been deleted from Totalmobile$")
  public void this_array_has_been_deleted_from_Totalmobile() throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @When("^this array has been deleted from the local tm_jobs table$")
  public void this_array_has_been_deleted_from_the_local_tm_jobs_table() throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

  @Then("^the system is empty$")
  public void the_system_is_empty() throws Exception {
    // Write code here that turns the phrase above into concrete actions
  }

}
