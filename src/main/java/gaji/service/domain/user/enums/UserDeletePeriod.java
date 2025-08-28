package gaji.service.domain.user.enums;

public enum UserDeletePeriod {
    ONE_DAY(1),
    ONE_WEEK(7),
    ONE_MONTH(30);

    private final int days;

    UserDeletePeriod(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
