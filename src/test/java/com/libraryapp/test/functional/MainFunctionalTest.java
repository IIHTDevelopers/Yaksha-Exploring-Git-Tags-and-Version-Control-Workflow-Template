package com.libraryapp.test.functional;

import static com.libraryapp.test.utils.TestUtils.businessTestFile;
import static com.libraryapp.test.utils.TestUtils.currentTest;
import static com.libraryapp.test.utils.TestUtils.testReport;
import static com.libraryapp.test.utils.TestUtils.yakshaAssert;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import mainapp.MyApp;

public class MainFunctionalTest {

    @AfterAll
    public static void afterAll() {
        testReport();
    }

    @Test
    @Order(1)
    public void testTagsExistence() throws IOException {
        try {
            // Check if tags v1.1.0, v1.1.1, and v1.2.0-beta exist
            String tagsOutput = MyApp.areTagsPresent();

            // Check if branches develop, hotfix-payment-gateway, and discount exist
            String branchesOutput = MyApp.areBranchesPresent();

            // Assert that all tags exist
            yakshaAssert(currentTest(), tagsOutput.equals("true") && branchesOutput.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(2)
    public void testHotfixBranchCreatedFromMain() throws IOException {
        try {
            // Check if hotfix-payment-gateway branch was created from main
            String hotfixBranchCreated = MyApp.wasBranchCreatedFromBaseBranch("discount", "hotfix-payment-gateway");

            // Check if develop branch was created from main
            String developBranchCreated = MyApp.wasBranchCreatedFromBaseBranch("hotfix-payment-gateway", "develop");

            // Check if discount branch was created from main
            String discountBranchCreated = MyApp.wasBranchCreatedFromBaseBranch("main", "discount");

            // Assert that the hotfix branch was created from main
            yakshaAssert(currentTest(), hotfixBranchCreated.equals("true") && developBranchCreated.equals("true") && discountBranchCreated.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(3)
    public void testAddDiscountCouponFeatureCommit() throws IOException {
        try {
            // Check if "adding file.txt" commit is present in 'master' branch
            String masterCommit = MyApp.isCommitPresentInBranch("main", "adding file.txt");

            // Check if "discount coupon feature" commit is present in 'discount' branch
            String discountCommit = MyApp.isCommitPresentInBranch("discount", "discount coupons feature");

            // Check if "payment gateway" commit is present in 'develop' branch
            String developCommit = MyApp.isCommitPresentInBranch("develop", "payment gateway");

            // Assert that both branches contain the commit
            yakshaAssert(currentTest(), developCommit.equals("true") && masterCommit.equals("true") && discountCommit.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }
}
