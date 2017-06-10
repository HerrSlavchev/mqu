'use strict';
 
angular.module('kohonenApp').factory('HttpService', ['$http', '$q', function($http, $q){
 
    var REST_SERVICE_URI = 'http://localhost:8080/';
 
    var api = {
        configureNetwork: configureNetwork,
        search: search
    };
 
    return api;
 
    function configureNetwork(config) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI+'configure/', config)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while configuring network.');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }
 
    function search(formData) {
    	var deferred = $q.defer();
        $http.post(
        		REST_SERVICE_URI+'search/', 
        		formData,
        		{
                    transformRequest: angular.identity,
                    headers: {
                      'Content-Type': undefined
                    }
                  }
        ).then(
	        function (response) {
	            deferred.resolve(response.data);
	        },
	        function(errResponse){
	            console.error('Error while searching.');
	            deferred.reject(errResponse);
	        }
        );
        return deferred.promise;
    }
 
}]);