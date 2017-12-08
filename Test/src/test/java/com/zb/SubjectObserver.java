package com.zb;

/**
 * Created by zhangbo on 17-8-31.
 */
public class SubjectObserver {
    private String subjectKey;
    private AbstractObserver abstractObserver;

    public String getSubjectKey() {
        return subjectKey;
    }

    public void setSubjectKey(String subjectKey) {
        this.subjectKey = subjectKey;
    }

    public AbstractObserver getAbstractObserver() {
        return abstractObserver;
    }

    public void setAbstractObserver(AbstractObserver abstractObserver) {
        this.abstractObserver = abstractObserver;
    }
}
