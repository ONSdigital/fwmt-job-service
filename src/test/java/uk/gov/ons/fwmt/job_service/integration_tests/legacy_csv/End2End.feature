Feature: End to end integration test
    
    End to end integration test
    
    Scenario: Recieving new staff member to staff endpoint
        Given the 'staff_2018-06-01T10:10:10Z.csv' .csv is posted to the '/staff' endpoint
        When the staff service returns HTTP status code 200
        Then the tm_users table contains the auth_no 'INT1'
        And the row has tm_username 'inttest'
        And the row qctive is 'true'
        
    Scenario: Recieving new GFF job to sample endpoint
        Given the 'sample_GFF_2018-06-01T01:01:01Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'NEWJOB1-001-806'
        And the row has last_auth_no 'INT1'
        
    Scenario: Recieving GFF reallocation job to sample endpoint
        Given the 'sample_GFF_2018-06-01T02:02:02Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'REALLOC1-001-806'
        And the row has last_auth_no 'INT1'
        
    Scenario: Recieving GFF reissue to sample endpoint
        Given the 'sample_GFF_2018-06-01T03:03:03Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'REISS1-001-806'
        And the row has last_auth_no 'INT1'
        
    Scenario: Recieving new LFS job to sample endpoint
        Given the 'sample_GFF_2018-06-01T01:01:01Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'NEWJOB1-001-806'
        And the row has last_auth_no 'INT1'
        
    Scenario: Recieving LFS reallocation job to sample endpoint
        Given the 'sample_GFF_2018-06-01T02:02:02Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'REALLOC1-001-806'
        And the row has last_auth_no 'INT1'
        
    Scenario: Recieving LFS reissue to sample endpoint
        Given the 'sample_GFF_2018-06-01T03:03:03Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'REISS1-001-806'
        And the row has last_auth_no 'INT1'
