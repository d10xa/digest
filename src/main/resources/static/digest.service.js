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
            getTasks: getTasks
        };

        return service;

        function schedule(digestData) {
            return $http.post("/digest/schedule", digestData);
        }
        
        function getTasks() {
            return $http.get("/digest/tasks");
        }
    }
})();
