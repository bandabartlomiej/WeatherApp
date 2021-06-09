package com.bwap.weatherapp.WeatherApp.views;
import com.bwap.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@SpringUI(path = "")
public class MainView extends UI {

    @Autowired
    private WeatherService weatherService;

    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button searchButton;
    private HorizontalLayout dashboard;
    private Label location;
    private Label currentTemp;
    private HorizontalLayout mainDescriptionLayout;
    private Label weatherDescription;
    private Label MaxWeather;
    private Label MinWeather;
    private Label Humidity;
    private Label Pressure;
    private Label Wind;
    private Label FeelsLike;
    private Image iconImg;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();

        searchButton.addClickListener(clickEvent -> {
            if(!cityTextField.getValue().equals("")) {
                try {
                    updateUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
                Notification.show("Wprowadz nazwe miasta");
        });





    }

    private void mainLayout() {
        iconImg = new Image();
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(mainLayout);
    }

    private void setHeader(){
        HorizontalLayout header0 = new HorizontalLayout();
        header0.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        HorizontalLayout header1 = new HorizontalLayout();
        header1.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        HorizontalLayout header2 = new HorizontalLayout();
        header2.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        Label projectTitle = new Label("Projekt: ");
        projectTitle.addStyleName(ValoTheme.LABEL_H1);
        projectTitle.addStyleName(ValoTheme.LABEL_BOLD);
        projectTitle.addStyleName(ValoTheme.LABEL_COLORED);
        Label title = new Label(" Aplikacja Pogodowa");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        Label author = new Label("Bartlomiej Banda");
        author.addStyleName(ValoTheme.LABEL_H3);
        author.addStyleName(ValoTheme.LABEL_COLORED);
        Label faculty = new Label("Informatyka w Mechatronice");
        faculty.addStyleName(ValoTheme.LABEL_H3);
        faculty.addStyleName(ValoTheme.LABEL_COLORED);
        header0.addComponent(projectTitle);
        header.addComponent(title);
        header1.addComponent(author);
        header2.addComponent(faculty);

        mainLayout.addComponents(header0,header,header1,header2);
    }

    private void setLogo(){
        HorizontalLayout logo = new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img = new Image(null, new ClassResource("/static/logo.png"));
        logo.setWidth("300px");
        logo.setHeight("300px");

        logo.addComponent(img);
        mainLayout.addComponent(logo);
    }

    private  void setForm(){
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);


        unitSelect = new NativeSelect<>();
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));
        formLayout.addComponent(unitSelect);


        cityTextField = new TextField();
        cityTextField.setWidth("80%");
        formLayout.addComponent(cityTextField);



        searchButton = new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(searchButton);





        mainLayout.addComponents(formLayout);


    }


    private  void dashboardTitle(){

        dashboard = new HorizontalLayout();
        dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);



        location = new Label("Aktualnie w Krakow");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);


        currentTemp = new Label("18"+"\u00b0"+"C");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);

        dashboard.addComponents(location, iconImg, currentTemp);
        mainLayout.addComponents(dashboard);

    }

    private void dashboardDetails(){

        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);



        VerticalLayout desriptionlayout = new VerticalLayout();
        desriptionlayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        weatherDescription = new Label("Opis: Czyste Niebo");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        desriptionlayout.addComponents(weatherDescription);


        MinWeather = new Label("Min:53");
        desriptionlayout.addComponents(MinWeather);

        MaxWeather = new Label("Max:53");
        desriptionlayout.addComponents(MaxWeather);

        VerticalLayout pressureLayout =  new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Pressure = new Label("Cisnienie: 1025hPa");
        pressureLayout.addComponents(Pressure);

        Humidity = new Label("Wilgotnosc: 55%");
        pressureLayout.addComponents(Humidity);

        Wind = new Label("Wiatr: 2");
        pressureLayout.addComponents(Pressure);

        FeelsLike = new Label("Odczuwalna: 3");
        pressureLayout.addComponents(FeelsLike);




        mainDescriptionLayout.addComponents(desriptionlayout, pressureLayout);

    }

    private void updateUI() throws JSONException {

        String city = cityTextField.getValue();
        location.setValue("Aktualnie w " + city);
        String defaultUnit;
        weatherService.setCityName(city);


        if(unitSelect.getValue().equals("F")){
            weatherService.setUnit("imperials");
            unitSelect.setValue("F");
            defaultUnit = "\u00b0"+"F";

        }else {
            weatherService.setUnit("metric");
            defaultUnit = "\u00b0"+"C";
            unitSelect.setValue("C");
        }




        JSONObject mainObject = weatherService.returnMain();
        int temp = mainObject.getInt("temp");
        currentTemp.setValue(temp+defaultUnit);



        String iconCode = null;
        String weatherDescriptionNew = null;
        JSONArray jsonArray = weatherService.returnWeatherArray();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject weatherObj = jsonArray.getJSONObject(i);
                iconCode = weatherObj.getString("icon");
                weatherDescriptionNew = weatherObj.getString("description");
                System.out.println(weatherDescriptionNew);

            }


        iconImg.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+"@2x.png"));
        weatherDescription.setValue("Opis:"+weatherDescriptionNew);
        MinWeather.setValue("Min Temp: "+weatherService.returnMain().getInt("temp_min")+"\u00b0"+unitSelect.getValue());
        MaxWeather.setValue("Max Temp: "+weatherService.returnMain().getInt("temp_max")+"\u00b0"+unitSelect.getValue());
        Pressure.setValue("Cisnienie: "+weatherService.returnMain().getInt("pressure")+"hPa");
        Humidity.setValue("Wilgotnosc: "+weatherService.returnMain().getInt("humidity")+"%");
        Wind.setValue("Wiatr: "+weatherService.returnWind().getInt("speed")+"m/s");
        FeelsLike.setValue("Odczuwalna Temp: "+weatherService.returnMain().getDouble("feels_like")+"\u00b0"+unitSelect.getValue());





        mainLayout.addComponents(dashboard, mainDescriptionLayout);


    }

}
