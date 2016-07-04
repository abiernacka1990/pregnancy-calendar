package biernacka.pregnancycalendar;

import android.app.Application;

import biernacka.pregnancycalendar.db.DiaryEntriesRepository;
import biernacka.pregnancycalendar.db.EventsRepository;
import biernacka.pregnancycalendar.db.WeightRepository;
import biernacka.pregnancycalendar.db.fake.FakeEventRepository;
import biernacka.pregnancycalendar.db.fake.FakeWeightRepository;
import biernacka.pregnancycalendar.db.ormlite.OrmDiaryEntryRepository;
import biernacka.pregnancycalendar.db.ormlite.OrmEventRepository;
import biernacka.pregnancycalendar.db.ormlite.OrmWeightRepository;
import biernacka.pregnancycalendar.utils.DevelopProperties;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by agnieszka on 03.11.15.
 * <p>
 * Application
 */
public class App extends Application {

    private static App instance;
    private WeightRepository weightRepository;
    private EventsRepository eventsRepository;
    private DiaryEntriesRepository diaryEntriesRepository;
    private DevelopProperties properties = new DevelopProperties();


    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        if (properties.useFakeWeightDatabase()) {
            weightRepository = new FakeWeightRepository();
        } else {
            weightRepository = new OrmWeightRepository(getApplicationContext());
        }
        if (properties.useFakeEventDatabase()) {
            eventsRepository = new FakeEventRepository();
        } else {
            eventsRepository = new OrmEventRepository(getApplicationContext());
        }
        diaryEntriesRepository = new OrmDiaryEntryRepository(getApplicationContext());
    }

    public WeightRepository getWeightRepository() {
        return weightRepository;
    }

    public EventsRepository getEventsRepository() {
        return eventsRepository;
    }

    public DiaryEntriesRepository getDiaryEntriesRepository() {
        return diaryEntriesRepository;
    }

    public String getDefaultWeightUnit() {
        return getApplicationContext().getString(R.string.default_weight_unit);
    }
}
