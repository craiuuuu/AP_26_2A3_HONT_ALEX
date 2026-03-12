    package lab3.advanced3;

    import lombok.Getter;
    import lombok.Setter;
    import lombok.ToString;

    import java.time.LocalDate;

    @Getter
    @Setter
    @ToString
    public class Programmer extends Person
    {
        private String favoriteLanguage;

        public Programmer(int id, String name, LocalDate birthDate, String nationality, String favoriteLanguage)
        {
            super(id, name, birthDate, nationality);
            this.favoriteLanguage = favoriteLanguage;
        }
    }
