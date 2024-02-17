import ru.dnlkk.tagger.util.parseLessonString

fun main() {
    val lessonStrings = listOf(
        "АиСД асс. Нужных А.В. 316П",
        "Интеллектуальные права в цифровых технологиях доц. Борисова А.А. (ДО)",
        "Электроника преп. Бажанова О.В. 420",
        "Язык программирования С++ (id=15462) ст.преп. Лысачев П.С. (ДО)",
        "УЦ",
        "Физическая культура и спорт",
        "Военная подготовка"
    )

    for (lessonString in lessonStrings) {
        val lesson = parseLessonString(lessonString)
        println("Subject: ${lesson.subject}, Teacher: ${lesson.teacher}, Location: ${lesson.location}")
    }
}
