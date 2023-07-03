Feature: Analyse text by plain input

  @edge
  Scenario Outline: Content text evaluation in Russian as native mode with expected mark <mark>
    Given User opens main page of PolyLing application using 'chrome'
    When User inputs text with <mark> mark into analyse area
    And User presses analyse button
    Then The score has attributes <mark>
    And The progress bar color is <color>
    Then User closes the page
    Examples:
      | mark | color       |
      | '1'  | 'red'       |
      | '2'  | 'yellow'    |
      | '3'  | 'redOrange' |
      | '4'  | 'orange'    |
      | '5'  | 'green'     |

  @edge
  Scenario Outline: Content text evaluation in Russian as foreign mode with expected mark <mark>
    Given User opens main page of PolyLing application using 'chrome'
    When User switches language to 'foreign' mode
    When User inputs text for foreign analyse with <mark> mark into analyse area
    And User presses analyse button
    Then The score of foreign mark has attributes <mark>
    And The progress bar color is <color>
    Then User closes the page
    Examples:
      | mark | color    |
      | '1'  | 'red'    |
      | '2'  | 'yellow' |
