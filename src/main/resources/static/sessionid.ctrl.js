(function() {
    'use strict';

    angular
        .module('app')
        .controller('SessionCtrl', SessionCtrl);

    SessionCtrl.$inject = ['$http','sessionId'];

    /* @ngInject */
    function SessionCtrl($http, sessionId) {
    console.log(sessionId);
        var vm = this;
        vm.id="";
        activate();

        function activate() {
            sessionId.get().then(function(response){
                vm.id = response.data.id;
            });

//            $http.get('/session/id').success(function(id){
//                vm.id = id;
//            });
        }
    }
})();
