//package com.crackit.springbatch.demo.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/crackit/v1/springbatchdemo")
//@RequiredArgsConstructor
//public class RunJobController {
//
//    private  final JobLauncher jobLauncher;
//    private  final Job job;
//
//    @Qualifier("exportJob")
//    private final Job exportJob;
//
//
//    @PostMapping("/jobs")
//    public void csvToDb() {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("startingAt:" , System.currentTimeMillis())
////                .addString("inputFilePath", "src/main/resources/Employee_Sample_Data.csv")
//                .addString("inputFilePath", "src/main/resources/Employee_Sample_Data.xlsx")
//
//                .toJobParameters();
//        try {
//            jobLauncher.run(job, jobParameters);
//        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
//                 JobParametersInvalidException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
////    @PostMapping("/export")
////    public void exportToExcel() {
////        try {
////            // Output file location
////            String outputPath = "src/main/resources/Exported_Employees.xlsx";
////
////            JobParameters jobParameters = new JobParametersBuilder()
////                    .addString("outputFilePath", outputPath)
////                    .addLong("startAt", System.currentTimeMillis())
////                    .toJobParameters();
////
////            jobLauncher.run(exportJob, jobParameters); // Ensure 'exportJob' is injected
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//
//    @PostMapping("/export")
//    public void exportToExcel() {
//        try {
//            // FIX: Ensure this matches the folder in your screenshot (D:\)
//            // Windows paths need double backslashes \\
//            String outputPath = "D:\\Exported_Employees.xlsx";
//
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addString("outputFilePath", outputPath)
//                    .addLong("startAt", System.currentTimeMillis())
//                    .toJobParameters();
//
//            jobLauncher.run(exportJob, jobParameters);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    // Inject the new job
//    @Qualifier("migrationJob")
//    private final Job migrationJob;
//
//    @PostMapping("/migrate-db")
//    public void migrateDb() {
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addLong("startAt", System.currentTimeMillis())
//                    .toJobParameters();
//
//            jobLauncher.run(migrationJob, jobParameters);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}

package com.crackit.springbatch.demo.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crackit/v1/springbatchdemo")
@RequiredArgsConstructor
public class RunJobController {

    private final JobLauncher jobLauncher;

    @Qualifier("importEmployeesJob")
    private final Job importEmployeesJob;

    @Qualifier("exportJob")
    private final Job exportJob;

    @Qualifier("migrationJob")
    private final Job migrationJob;

    // ... your 3 endpoints calling the 3 jobs above ...

    @PostMapping("/migrate-db")
    public void migrateDb() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(migrationJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}