package sit.tuvarna.bg.first_app;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    public static <T> List<T> extractEntitiesFromSheet(Sheet sheet, RowMapper<T> rowMapper) {
        List<T> entities = new ArrayList<>();
        //почвам от 2рия ред защото първия е за имената на колоните
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                entities.add(rowMapper.mapRow(row));
            }
        }
        return entities;
    }

    //интерфейс за да мога чрез lambda expression да имплементирам функцията
    public interface RowMapper<T> {
        T mapRow(Row row);
    }
}
