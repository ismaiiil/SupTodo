package com.supinfo.and.suptodo.SQLITE;

import android.os.AsyncTask;

public class SQLITEAsyncTasks {
    public static class InsertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        public InsertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    public static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao mAsyncTaskDao;
        private DeleteAllCompletionHandler mCH;

        public DeleteAllAsyncTask(UserDao dao, DeleteAllCompletionHandler mCH) {
            mAsyncTaskDao = dao;
            this.mCH = mCH;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mCH != null){
                mCH.onFinished();
            }
        }
    }

    public static class GetCachedUserAsyncTask extends android.os.AsyncTask<Void, Void, User> {

        private UserDao mAsyncTaskDao;
        GetUserCompletionHandler guH;

        public GetCachedUserAsyncTask(UserDao dao, GetUserCompletionHandler getUserCompletionHandler) {
            mAsyncTaskDao = dao;
            guH = getUserCompletionHandler;
        }

        @Override
        protected User doInBackground(Void... voids) {
            return mAsyncTaskDao.getCachedUser();
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            guH.onFinished(user);
        }
    }


}
