/*
 *    Copyright (C) 2016 Tamic
 *
 *    link :
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.nolovr.nolohome.statistics.model;


import java.util.List;

/**
 * 上传数据对象bean
 */
public class DataBlock {
    private List<AppAction> app_action ;
    private List<Page> page ;
    private List<Event> event ;
    private List<ExceptionInfo>exceptionInfos;


    public List<ExceptionInfo> getExceptionInfos() {
        return exceptionInfos;
    }

    public void setExceptionInfos(List<ExceptionInfo> exceptionInfos) {
        this.exceptionInfos = exceptionInfos;
    }

    public List<AppAction> getApp_action() {
        return app_action;
    }

    public void setApp_action(List<AppAction> app_action) {
        this.app_action = app_action;
    }

    public List<Page> getPage() {
        return page;
    }

    public void setPage(List<Page> page) {
        this.page = page;
    }

    public List<Event> getEvent() {
        return event;
    }

    public void setEvent(List<Event> event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataBlock dataBlock = (DataBlock) o;

        if (!app_action.equals(dataBlock.app_action)) return false;
        if (!page.equals(dataBlock.page)) return false;
        if (!event.equals(dataBlock.event)) return false;
        return exceptionInfos.equals(dataBlock.exceptionInfos);

    }

    @Override
    public int hashCode() {
        int result = app_action.hashCode();
        result = 31 * result + page.hashCode();
        result = 31 * result + event.hashCode();
        result = 31 * result + exceptionInfos.hashCode();
        return result;
    }
}
