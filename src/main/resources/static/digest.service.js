(function() {
    'use strict';

    angular
        .module('app')
        .factory('Digest', Digest);

    Digest.$inject = ['$http'];

    /* @ngInject */
    function Digest($http) {
        var service = {
            schedule: schedule,
            getTasks: getTasks,
            cancel: cancel,
            remove: remove
        };

        return service;

        function schedule(digestData) {
            return $http.post("/digest/schedule", digestData);
        }
        
        function getTasks() {
            return $http.get("/digest/tasks");
        }

        function cancel(taskId){
            return $http.post(["/digest/tasks/", taskId, "/cancel"].join(''))
        }

        function remove(taskId){
            return $http['delete'](["/digest/tasks/", taskId].join(''))
        }
    }
})();
