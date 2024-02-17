package ru.dnlkk.tagger.util

data class LessonParserToken(val subject: String, val teacher: String?, val location: String?)

val stupidLessons = listOf(
    "Математический анализ Вноградова",
    "Комплексный анализ Санина",
    "Криптографиечкские стандарты Текунов",
    "Функциональный анализ Прошунин А.И",
    "Русский язык и культура речи",
    "Защита программ и данных",
    "Бутузов СПиБКС и С",
    "Введение в специальность Кенин",
    "Математический анализ  Украинский"
    )

private fun specialParse(lessonString: String): String {
    for (stupidLesson in stupidLessons) {
        if (lessonString.startsWith(stupidLesson)) {
            return when(stupidLesson) {
                stupidLessons[0] -> "Математический анализ доц. Виноградова Г.А.${lessonString.substring(stupidLesson.length)}"
                stupidLessons[1] -> "Комплексный анализ доц. Санина Е.Л.${lessonString.substring(stupidLesson.length)}"
                stupidLessons[2] -> "Криптографиечкские стандарты доц. Текунов В.В.${lessonString.substring(stupidLesson.length)}"
                stupidLessons[3] -> "Функциональный анализ доц. Прошунин А.И.${lessonString.substring(stupidLesson.length)}"
                stupidLessons[4] -> "Функциональный анализ доц.${lessonString.substring(stupidLesson.length)}"
                stupidLessons[5] -> "Защита программ и данных доц. Иванкин М.П. (ДО)"
                stupidLessons[6] -> "СПиБКС и С доц. Бутузов В.В.${lessonString.substring(stupidLesson.length)}"
                stupidLessons[7] -> "Введение в специальность доц. Кенин С.Л. (ДО)"
                stupidLessons[8] -> "Математический анализ асс. Украинский П.С.${lessonString.substring(stupidLesson.length)}"
                else -> lessonString
            }
        }
    }
    return lessonString
}

fun parseLessonString(lessonString: String): LessonParserToken {
    if (lessonString == "ДО") {
        return LessonParserToken("", null, null)
    }
    val special: String = specialParse(lessonString)
    val parts = special.split(
        "ст. преп.",
        "преп.",
        "асс.",
        "доц.",
        "доц. доц.",
        "ст.преп.",
        "проф.",
    )
    val subject = parts[0].trim()
    if (parts.size > 1) {
        val teacherAndLocation = parts[1].split(" ").filter { it.isNotBlank() }
        val teacher = teacherAndLocation.takeWhile { it[0].isLetter() }.joinToString(" ")
        val location = teacherAndLocation.dropWhile { it[0].isLetter() }.joinToString(" ")
        return LessonParserToken(subject, teacher, location)
    }

    return LessonParserToken(subject, null, null)
}