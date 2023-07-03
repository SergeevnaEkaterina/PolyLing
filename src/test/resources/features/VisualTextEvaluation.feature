Feature: Visual text evaluation

  @functional
  Scenario Outline: Analyse by file with expected mark <mark>
    Given Upload generated file with <mark> mark with the following criteria considered <criteria> via API
    When Grade is <mark> with overallComment <comment>
    When The following criteria in true mode: <criteria>

    Examples:
      | mark | comment                                                                           | criteria             |
      | '3'  | 'Ой! Стоит подумать об оформлении...'                                             | '2,3,8'              |
      | '4'  | 'Ой! Стоит подумать об оформлении...'                                             | '3,5,6,8'            |
      | '5'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '2,3,5,6,7'          |
      | '6'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '2,3,5,6,7,8'        |
      | '6'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '2,3,6,7,8,9'        |
      | '7'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '2,3,5,6,7,8,10'     |
      | '8'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '0,2,3,5,6,7,8,9'    |
      | '9'  | 'Поздравляем! Текст читается хорошо!'                                             | '0,2,3,5,6,7,8,9,10' |

  @functional
  Scenario Outline: Analyse by link successful scenario with expected mark <mark>
    Given Upload text to analyse by link <link> via API
    When Grade by link is <mark> with overallComment <comment>
    When The following criteria by link in true mode: <criteria>

    Examples:
      | link                                                                                                                    | mark | comment                                                                           | criteria               |
      | 'http://lesgaft.spb.ru/'                                                                                                | '8'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '1,2,3,4,6,8,9,10'     |
      | 'https://narfu.ru/life/news/university/373117/'                                                                         | '6'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '3,4,5,6,8,9'          |
      | 'https://spbu.ru/news-events/novosti/vrachi-spbgu-smogli-polnostyu-udalit-slozhnyy-recidiv-opuholi-provedya'            | '9'  | 'Поздравляем! Текст читается хорошо!'                                             | '2,3,4,5,6,7,8,9,10'   |
      | 'https://bsuedu.ru/bsu/news/news.php?ID=770729&IBLOCK_ID=176'                                                           | '7'  | 'Довольно хорошо! Скорректируйте пункты, отмеченные красным, и станет еще лучше!' | '2,4,5,6,7,8,9'        |
      | 'https://www.sechenov.ru/pressroom/news/-rossiyskaya-razrabotka-nauchit-spasat-realnye-zhizni-v-virtualnoy-realnosti-/' | '10' | 'Поздравляем! Текст читается хорошо!'                                             | '1,2,3,4,5,6,7,8,9,10' |

  @functional
  Scenario Outline: Analyse by link negative scenario by not existing link <link>
    Given Upload text by not enabled link <link> via API and should get error

    Examples:
      | link                                                                  |
      | 'https://library.spbstu.ru/ru/pages/chitateliam/kak_stat_chitatelem/' |
      | 'https://www.voenmeh.ru/'                                             |
      | 'https://ulsu.ru/ru/page/page_23/'                 |
      | 'https://old.ulstu.ru/main?cmd=index&design=aspir' |
      | 'https://new.guap.ru/targets/studs'                |
