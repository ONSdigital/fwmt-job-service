package uk.gov.ons.fwmt.job_service.integration_tests.legacy_csv;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.And;

public class End2End {
  
  @Given("^the 'staff_(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+)Z\\.csv' \\.csv is posted to the '/staff' endpoint$")
  public void the_staff__T_Z_csv_csv_is_posted_to_the_staff_endpoint(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) throws Exception {
      // Write code here that turns the phrase above into concrete actions
      
  }

  @When("^the staff service returns HTTP status code (\\d+)$")
  public void the_staff_service_returns_HTTP_status_code(int arg1) throws Exception {
      // Write code here that turns the phrase above into concrete actions
      
  }

  @Then("^the tm_users table contains the auth_no 'INT(\\d+)'$")
  public void the_tm_users_table_contains_the_auth_no_INT(int arg1) throws Exception {
      // Write code here that turns the phrase above into concrete actions
      
  }

  @Then("^the row has tm_username 'inttest'$")
  public void the_row_has_tm_username_inttest() throws Exception {
      // Write code here that turns the phrase above into concrete actions
      
  }

  @Then("^the row qctive is 'true'$")
  public void the_row_qctive_is_true() throws Exception {
      // Write code here that turns the phrase above into concrete actions
      
  }

  @Given("^the 'sample_GFF_(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+)Z\\.csv' \\.csv is posted to the '/samples' endpoint$")
  public void the_sample_GFF__T_Z_csv_csv_is_posted_to_the_samples_endpoint(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) throws Exception {
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
  
}
