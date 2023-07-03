Feature: Smoke tests

  @smoke
  Scenario: Validate information section
    Given User opens main page of PolyLing application using 'chrome'
    When User minimizes window to mobile mode
    Then User presses burger button at right up corner
    Then Headers in mobile version are
      | Оценка текста | Команда проекта | Материалы исследования | О проекте |
    Then User closes the page
  @smoke
  Scenario Outline: Get site pages
    Given User successfully gets <name> page of PolyLing application
    When Response body is valid
    Examples:
      | name           |
      | 'materials'    |
      | 'participants' |
      | 'project'      |
  @smoke
  Scenario: Validate text page
    Given User opens main page of PolyLing application using 'chrome'
    Then The main header is 'Оценка воспринимаемости мультимодального электронного текста'
    When The content to visual switcher is available
    Then The field for text is available with text 'вставьте текст длиной от 1000 до 10000 символов'
    When Language nativity ratios are available
    When The analyse button is available
    Then User inputs text 'test' into analyse area
    Then Char counter shows '4' quantity
    Then User switches visual perception
    Then The link input field is available with text 'domain.tld'
    When The analyse button is available
    When The content to visual switcher is available
    Then Switch to doc input
    When The analyse button is available
    When The content to visual switcher is available
    When The choose file button is available
    Then User closes the page


