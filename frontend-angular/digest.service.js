(function () {
    'use strict';

    angular
        .module('app')
        .factory('Digest', Digest);

    Digest.$inject = ['$http', 'serverUrl'];

    /* @ngInject */
    function Digest($http, serverUrl) {
        var service = {
            schedule: schedule,
            getTasks: getTasks,
            cancel: cancel,
            remove: remove
        };

        return service;

        function schedule(digestData) {
            return $http.post([serverUrl, "/digest/schedule"].join(''), digestData);
        }

        function getTasks() {
            return $http.get([serverUrl, "/digest/tasks"].join(''));
        }

        function cancel(taskId) {
            return $http.post([serverUrl, "/digest/tasks/", taskId, "/cancel"].join(''))
        }

        function remove(taskId) {
            return $http['delete']([serverUrl, "/digest/tasks/", taskId].join(''))
        }
    }
})();
