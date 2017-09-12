package    yh.ai.dependencyinjection;

public enum GamePropertiesParameter {
    DEMO, PHASE1, PHASE2, PHASE3,TEST;//add a test 

    public static GamePropertiesParameter fromString(String s) {
        if (s.equals("phase1")) {
            return PHASE1;
        } else if (s.equals("phase2")) {
            return PHASE2;
        } else if (s.equals("phase3")) {
            return PHASE3;
        }else if(s.equals("test")) {
        	return TEST;
        }
        return DEMO;
    }
}
