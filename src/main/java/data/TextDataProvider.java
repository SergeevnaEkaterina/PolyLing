package data;

import model.request.TextBody;

public class TextDataProvider {

    private static String TEXT = "К сожалению, мы не можем быть уверены, что разработчик исправил все найденные проблемы внутри IDE перед коммитом. Один из способов гарантировать отсутствие критичных проблем в коде — внедрить Qodana прямо в CI/CD-пайплайн. Если нельзя пофиксить все сразу, вы сможете выбрать критичные проблемы, добавить их в baseline и постепенно разбирать тех. долг, не замедляя разработку, но при этом контролируя появление новых проблем.\n" +
            "            \"Иногда релизный процесс сложен и зависит от множества ручных действий. Часто это целиком ручной релиз: какой-то артефакт собирается одним из разработчиков, потом сборка передается тестировщикам и проверяется, затем передается человеку, который умеет доставлять ее на боевое окружение. Здесь есть много узких мест — представьте, если кто-то из этих людей уйдет в отпуск или заболеет. \n" +
            "            \"Выводится релиз-кандидат, который собирается с релиз-ветки или с главной ветки — это фиксирует версию и позволяет быть уверенным, что на боевое окружение попадет только то, что было протестировано и никак не менялось в процессе. Это помогает отслеживать все релизы и изменения, которые в них попали, и сохранять артефакты стабильной версии для быстрого отката в случае неудачного релиза.\n" +
            "\n";
    private static String RESULT = "*Текст легок для восприятия.*\n\nПоздравляем! Ваш текст идеален! Однако не забудьте о визуальном оформлении Вашего текста. Проверить уровень визуальной воспринимаемости текста Вы можете в специальном разделе нашего сервиса.\n";
    static TextBody body = new TextBody(true, TEXT);

}
