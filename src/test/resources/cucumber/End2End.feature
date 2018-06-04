Feature: End to end integration test
    
    End to end integration test
    
    Scenario: Create user for integration testing
        Given two usernames
        |"inttest0"|
        |"inttest1"|
        And auth_nos
        |"INT0"|
        |"INT1"|
        Then create the users in the tm_jobs table
    # THESE USERS MUST ALREADY EXIST AND HAVE A RESOURCE IN TOTALMOBILE
        
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
        Given the 'sample_LFS_2018-06-01T01:01:01Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'NEWJOB1 1 1 1 1 1 1 1 - 81K'
        And the row has last_auth_no 'INT1'
        
    Scenario: Recieving LFS reallocation job to sample endpoint
        Given the 'sample_LFS_2018-06-01T02:02:02Z.csv' .csv is posted to the '/samples' endpoint
        When the sample service returns HTTP status code 200
        Then the tm_jobs table contains the tm_job_id 'REALLOC1 1 1 1 1 1 1 1 - 81K'
        And the row has last_auth_no 'INT1'

    Scenario: Clearing down integration test jobs
        Given tm_job_ids array
        |"NEWJOB1-001-806"|
        |"REALLOC1-001-806"|
        |"REISS1-001-806"|
        |"NEWJOB1 1 1 1 1 1 1 1 - 81K"|
        |"REALLOC1 1 1 1 1 1 1 1 - 81K"|
        When this array has been deleted from Totalmobile
        And this array has been deleted from the local tm_jobs table
        Then the system is empty