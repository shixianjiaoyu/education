package cn.sjjy.edu.auth.springmvc;


import java.util.Set;

public class SolarInfo {

    private int userId;
    private String userName;
    private Set<String> points;

    public int getUserId() {
        return userId;
    }

    void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<String> getPoints() {
        return points;
    }

    void setPoints(Set<String> points) {
        this.points = points;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private int userId;
        private String userName;
        private Set<String> points;

        private Builder() {
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder points(Set<String> points) {
            this.points = points;
            return this;
        }

        public SolarInfo build() {
            SolarInfo solarInfo = new SolarInfo();
            solarInfo.setUserId(userId);
            solarInfo.setUserName(userName);
            solarInfo.setPoints(points);
            return solarInfo;
        }
    }
}
