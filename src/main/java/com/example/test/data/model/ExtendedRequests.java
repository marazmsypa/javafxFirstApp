package com.example.test.data.model;



import java.util.Date;


public class ExtendedRequests {
    private Integer id;
    private RequestTypes request_type;
    private RequestStatuses request_status;

    private Date date_start;

    private Date date_end;

    private VisitPurposes visit_purpose;

    private Employees employee;

    private Integer group_id;

    private Visitors visitor;

    private boolean _group;

    private String message;

    public ExtendedRequests() {
    }

    public ExtendedRequests(Integer id, RequestTypes request_type, RequestStatuses request_status, Date date_start, Date date_end, VisitPurposes visit_purpose, Employees employee, Integer group_id, Visitors visitor, boolean _group, String message) {
        this.id = id;
        this.request_type = request_type;
        this.request_status = request_status;
        this.date_start = date_start;
        this.date_end = date_end;
        this.visit_purpose = visit_purpose;
        this.employee = employee;
        this.group_id = group_id;
        this.visitor = visitor;
        this._group = _group;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RequestTypes getRequest_type() {
        return request_type;
    }

    public void setRequest_type(RequestTypes request_type) {
        this.request_type = request_type;
    }

    public RequestStatuses getRequest_status() {
        return request_status;
    }

    public void setRequest_status(RequestStatuses request_status) {
        this.request_status = request_status;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public VisitPurposes getVisit_purpose() {
        return visit_purpose;
    }

    public void setVisit_purpose(VisitPurposes visit_purpose) {
        this.visit_purpose = visit_purpose;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Visitors getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitors visitor) {
        this.visitor = visitor;
    }

    public boolean is_group() {
        return _group;
    }

    public void set_group(boolean _group) {
        this._group = _group;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ExtendedRequests{" +
                "id=" + id +
                ", request_type=" + request_type +
                ", request_status=" + request_status +
                ", date_start=" + date_start +
                ", date_end=" + date_end +
                ", visit_purpose=" + visit_purpose +
                ", employee=" + employee +
                ", group_id=" + group_id +
                ", visitor=" + visitor +
                ", _group=" + _group +
                ", message='" + message + '\'' +
                '}';
    }
}
