# Insider UI Automation Test Report
This document summarizes the complete UI automation coverage implemented for the Insider website, focusing on Home Page components, navigation menu functionality, Careers → Quality Assurance job filtering, negative filtering scenarios, and View Role redirection to Lever Application Form pages.

---

# 📌 1. Project Scope

This automation covers the following workflows:

- Insider Home Page UI validation
- Navigation bar tab & sub-item validation
- Careers Page sections validation
- QA job filtering (positive case)
- QA job filtering (negative case: no job results)
- View Role button redirection to Lever job application pages

The code is structured using:

- **Page Object Model (POM)**
- **Selenium WebDriver**
- **TestNG**
- **Allure Reporting**
- **Custom Log Utility**
- **Dynamic locator & filter selection**
- **Multi-tab handling**

---

# 📌 2. Automation Architecture Overview

### Key Components:
- **BasePage**  
  Contains shared methods: click, scroll, wait, URL validation, JS actions, switching tabs, etc.

- **InsiderHomePage (Page Object)**  
  Contains:
    - Navigation Bar locators
    - Home Page section locators
    - Careers Page section locators
    - “See all QA jobs” button
    - Dynamic filter dropdown selectors
    - Job card content (position, department, location)
    - “No positions available.” message locator
    - “View Role” button locator
    - All verification and action methods for QA jobs

- **HomePageTests (Test Class)**  
  Contains 4 separate test cases, each representing one functional area.

---

# 📌 3. Implemented Page Object Methods

## ✔ Navigation
- `selectNavBarTabItem(String tab)`
- `selectNavBarSubItem(String subItem)`

## ✔ Home Page Validation
- `verifyHomePageSections()`

Validates:
- Header Navigation
- Desktop Hero
- Logo Reel
- Case Studies
- One Platform Infinite CX
- Journey Section
- Sirius AI Section
- Tabbed Content
- Testimonials
- Integrations
- CTA Banner
- Footer

## ✔ Careers Page Validation
- `verifyCareerPageSections()`

Validates:
- Find Our Calling
- Why Become One of Us
- Our Locations
- Find Job Widget
- Life at Insider
- Footer

## ✔ QA Jobs Filtering
- `selectLocation(String location)`
- `selectDepartment(String department)`
- `selectFilterOption(...)`
- `verifyJobsListAppears()`
- `verifyJobCardsMatchFilters(...)`

## ✔ Negative Filtering Scenario
- `verifyNoPositionsAvailableMessage()`

## ✔ View Role Button Redirection
- `clickViewRoleButtonsInLoop()`
    - Iterates over all job cards
    - Clicks **View Role**
    - Switches to new tab
    - Verifies redirect to Lever (`jobs.lever.co/useinsider/...`)
    - Closes new tab
    - Returns to original tab

---

# 📌 4. Test Case Summary

## ✅ **Test Case 1 — Verify Home Page Sections**
**Test Method:** `verifyNavigationHomePage_Case_1`  
**Purpose:** Ensures that Insider Home Page loads properly.

### Steps:
1. Navigate to https://useinsider.com/
2. Validate presence of all main Home Page UI blocks.

### Expected Result:
All homepage sections are displayed correctly.

---

## ✅ **Test Case 2 — Verify Company → Careers Navigation**
**Test Method:** `verifyNavigationTabAndSubItem_Case_2`  
**Purpose:** Verify navigation to Careers Page.

### Steps:
1. Open home page
2. Click **Company** tab
3. Select **Careers**
4. Verify Careers Page sections

### Expected Result:
Careers page loads and all UI blocks are visible.

---

## ✅ **Test Case 3 — QA Job Filtering (Positive Scenario)**
**Test Method:** `verifyQaJobFiltering_Case_3`  
**Purpose:** Filter QA positions by Location and Department.

### Steps:
1. Navigate to QA Careers Page
2. Click **See all QA jobs**
3. Apply:
    - Location → Istanbul, Turkiye
    - Department → Quality Assurance
4. Validate:
    - Job list appears
    - Each job card matches:
        - Department: Quality Assurance
        - Location: Istanbul, Turkey

### Expected Result:
Only QA jobs in Istanbul appear.

---

## ✅ **Test Case 3.1 — QA Job Filtering (Negative Scenario)**
**Test Method:** `verifyQaJobFilteringNoPosition_Case_3_1`  
**Purpose:** Filtering for no results.

### Steps:
1. Navigate to QA page
2. Click **See all QA jobs**
3. Apply:
    - Location → Chile, Chile
    - Department → Quality Assurance
4. Validate:
    - “No positions available.” message is shown

### Expected Result:
No job card appears and correct empty result message is displayed.

---

## ✅ **Test Case 4 — Verify View Role Redirection**
**Test Method:** `verifyQaJobFilteringViewRoleActions_Case_4`  
**Purpose:** Validate that each job’s **View Role** leads to Lever Application Form.

### Steps:
1. Filter jobs (same filters as Case 3)
2. For each job card:
    - Click “View Role”
    - Check new tab opens
    - Check URL contains: `jobs.lever.co/useinsider`
    - Close tab and continue

### Expected Result:
All View Role buttons redirect to valid Lever application pages.

---

# 📌 5. Coverage Confirmation Against Requirements

| Requirement | Covered? | Details |
|------------|----------|---------|
| Visit Home Page & verify it opens | ✅ | Case 1 |
| Navigate Company → Careers | ✅ | Case 2 |
| Verify Careers page blocks | ✅ | Case 2 |
| Filter QA jobs | ✅ | Case 3 |
| Validate job list | ✅ | Case 3 |
| Position/Department/Location match | ✅ | Case 3 & extended method |
| No-position message visible | ✅ | Case 3.1 |
| View Role redirects to Lever | ✅ | Case 4 |

All requirements are fully automated.

---

# 📌 6. Additional Notes & Improvements

- Framework successfully supports multi-tab handling.
- Dropdown selection is fully dynamic.
- Page Object locators are stable and scalable.
- All test flows include descriptive Allure @Steps for rich reporting.
- Logging uses `Log.pass()` and `Log.fail()` consistently.

---

# 📌 7. Conclusion

This full automation suite provides reliable coverage for Insider’s Home Page, Careers Page, and QA Hiring workflow, validating:

- UI component availability
- Navigation accuracy
- Dynamic filtering
- Multi-tab external job redirects
- Negative filtering behavior

All scenarios are POM-structured, scalable, and aligned with best QA automation practices.

