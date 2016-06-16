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
            countAll: countAll
        };

        return service;

        function schedule(digestData) {
            return $http.post("/digest/schedule", digestData);
        }

        function countAll(digestData) {
            return $http.get("/digest/countAll");
        }
    }
})();
