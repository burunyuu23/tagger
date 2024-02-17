import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.DayOfWeek

@Converter(autoApply = true)
class DayOfWeekConverter : AttributeConverter<DayOfWeek, String> {
    override fun convertToDatabaseColumn(attribute: DayOfWeek?): String? {
        return attribute?.toString()
    }

    override fun convertToEntityAttribute(dbData: String?): DayOfWeek? {
        return dbData?.let { DayOfWeek.valueOf(it) }
    }
}